// src/main/java/com/mongletrip/mongletrip_backend/trip/TripService.java

package com.mongletrip.mongletrip_backend.trip;

import com.mongletrip.mongletrip_backend.common.exception.ResourceNotFoundException;
import com.mongletrip.mongletrip_backend.domain.trip.*; // Trip, Expense, Candidate 등 모든 도메인 엔티티 통합
import com.mongletrip.mongletrip_backend.domain.trip.Expense.ExpenseType; // ExpenseType Enum 명시적 import
import com.mongletrip.mongletrip_backend.domain.user.User;
import com.mongletrip.mongletrip_backend.trip.dto.*; // 모든 DTO 통합
import com.mongletrip.mongletrip_backend.trip.dto.BudgetStatusResponse.ExpenseDetail;
import com.mongletrip.mongletrip_backend.trip.dto.BudgetStatusResponse.PersonalBudgetDetail;
import com.mongletrip.mongletrip_backend.trip.dto.DateStatusResponse.DateMatchInfo;
import com.mongletrip.mongletrip_backend.trip.dto.ItineraryListResponse.ItineraryItemDetail;
import com.mongletrip.mongletrip_backend.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime; // 🚨 지출 기록에 사용
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mongletrip.mongletrip_backend.domain.trip.ChecklistItem;
import com.mongletrip.mongletrip_backend.domain.trip.ChecklistItem.ChecklistType;
import com.mongletrip.mongletrip_backend.trip.dto.ChecklistRequest;
import com.mongletrip.mongletrip_backend.trip.dto.ChecklistListResponse;
import com.mongletrip.mongletrip_backend.trip.dto.ChecklistListResponse.ChecklistDetail;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final TripMemberRepository tripMemberRepository;
    private final UserRepository userRepository;
    private final AvailableDateRepository availableDateRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateVoteRepository candidateVoteRepository;
    private final ItineraryItemRepository itineraryItemRepository;
    private final ExpenseRepository expenseRepository;
    private final PersonalBudgetRepository personalBudgetRepository;
    private final ChecklistItemRepository checklistItemRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d");

    // 5. 방 개설 - 새로운 여행 방 생성
    @Transactional
    public TripCreateResponse createTrip(Long userId, TripCreateRequest request) {
        // 1. 방장(Creator) 사용자 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. 고유 초대 코드 생성 (8자리)
        String inviteCode = generateUniqueInviteCode();

        // 3. Trip 엔티티 생성 및 저장
        Trip trip = Trip.builder()
                .name(request.getName())
                .destination(request.getDestination())
                .inviteCode(inviteCode)
                .creatorId(userId)
                .build();
        tripRepository.save(trip);

        // 4. 방장(Creator)을 TripMember로 즉시 등록
        TripMember creatorMember = TripMember.builder()
                .trip(trip)
                .user(user)
                .isCreator(true)
                .build();
        tripMemberRepository.save(creatorMember);

        return TripCreateResponse.builder()
                .tripId(trip.getId())
                .inviteCode(inviteCode)
                .message("여행 방이 성공적으로 생성되었습니다!")
                .build();
    }

    // 5. 방 개설 - 초대 코드로 여행 참여
    @Transactional
    public void joinTripByInviteCode(Long userId, String inviteCode) {
        // 1. 초대 코드로 Trip 찾기
        Trip trip = tripRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new ResourceNotFoundException("유효하지 않은 초대 코드입니다."));

        // 2. 사용자 및 중복 참여 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        if (tripMemberRepository.findByTripAndUserId(trip, userId).isPresent()) {
            throw new IllegalArgumentException("이미 참여하고 있는 여행입니다.");
        }

        // 3. TripMember로 등록
        TripMember newMember = TripMember.builder()
                .trip(trip)
                .user(user)
                .isCreator(false)
                .build();
        tripMemberRepository.save(newMember);
    }

    // 4. 메인메뉴 - 사용자의 여행 목록 조회
    @Transactional(readOnly = true)
    public List<TripListResponse> getTripsByUserId(Long userId) {
        // 1. 사용자가 참여하고 있는 모든 TripMember 관계 조회
        List<TripMember> tripMembers = tripMemberRepository.findByUserId(userId);

        // 2. Trip 정보를 추출하고 DTO로 변환
        return tripMembers.stream()
                .map(tm -> {
                    Trip trip = tm.getTrip();

                    // 날짜 표시 포맷팅
                    String dateRange = trip.getStartDate() != null && trip.getEndDate() != null
                            ? trip.getStartDate().format(DATE_FORMATTER) + " ~ " + trip.getEndDate().format(DATE_FORMATTER)
                            : "날짜 미정";

                    // 참여 멤버 수 계산
                    int memberCount = tripMemberRepository.countByTrip(trip);

                    return TripListResponse.builder()
                            .tripId(trip.getId())
                            .name(trip.getName())
                            .destination(trip.getDestination())
                            .dateRange(dateRange)
                            .memberCount(memberCount)
                            .progress(trip.getProgress())
                            .inviteCode(trip.getInviteCode())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 고유한 8자리 초대 코드 생성 로직
    private String generateUniqueInviteCode() {
        String code;
        do {
            // UUID에서 하이픈을 제거하고 앞 8자리를 사용
            code = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
        } while (tripRepository.findByInviteCode(code).isPresent()); // 중복되면 다시 생성
        return code;
    }

    // API 6: 여행 방 삭제
    @Transactional
    public void deleteTrip(Long userId, Long tripId) {
        // 1. Trip 조회
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("삭제할 여행 방을 찾을 수 없습니다."));

        // 2. 권한 확인 (방장 여부)
        if (!trip.getCreatorId().equals(userId)) {
            // 권한이 없음을 알리는 커스텀 예외 (403 Forbidden 등으로 처리될 예정)
            throw new RuntimeException("여행 방을 삭제할 권한이 없습니다. (방장만 가능)");
        }

        // 3. 연관 데이터 삭제 (TripMember 등)
        tripMemberRepository.deleteByTrip(trip);

        // 4. Trip 삭제
        tripRepository.delete(trip);
    }

    // API 6: 내 가능 날짜 등록/수정 (PUT /api/trips/{tripId}/available-dates)
    @Transactional
    public void updateAvailableDates(Long userId, Long tripId, AvailableDateRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 1. 기존 데이터 모두 삭제 (사용자가 날짜를 갱신할 때마다 기존 것을 지우고 새로 저장)
        availableDateRepository.deleteByTripAndUserId(trip, userId);

        // 2. 새 데이터 저장 (요청 목록이 비어있으면 저장하지 않음)
        if (request.getPossibleDates() != null && !request.getPossibleDates().isEmpty()) {
            List<AvailableDate> newDates = request.getPossibleDates().stream()
                    .map(date -> AvailableDate.builder()
                            .trip(trip)
                            .user(user)
                            .possibleDate(date)
                            .build())
                    .collect(Collectors.toList());
            availableDateRepository.saveAll(newDates);
        }
    }

    // API 7: 날짜 합의 현황 조회 (GET /api/trips/{tripId}/date-status)
    @Transactional(readOnly = true)
    public DateStatusResponse getDateStatus(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        // 1. 전체 멤버 수 계산
        int totalMemberCount = tripMemberRepository.countByTrip(trip);
        if (totalMemberCount == 0) {
            return DateStatusResponse.builder()
                    .totalMemberCount(0)
                    .aiRecommendations(Collections.emptyList())
                    .memberPossibilities(Collections.emptyList())
                    .build();
        }

        // 2. 전체 AvailableDate 조회
        List<AvailableDate> allAvailableDates = availableDateRepository.findByTrip(trip);

        // 3. 날짜별 매칭 현황 집계 (Map<LocalDate, List<User>>)
        Map<LocalDate, Long> dateCountMap = allAvailableDates.stream()
                .collect(Collectors.groupingBy(
                        AvailableDate::getPossibleDate,
                        Collectors.counting()
                ));

        // 4. 날짜별 매칭 정보 DTO 생성 및 AI 추천 로직 적용
        List<DateMatchInfo> allMatchInfos = dateCountMap.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    int count = entry.getValue().intValue();
                    int percentage = (int) Math.round((double) count / totalMemberCount * 100);

                    return DateMatchInfo.builder()
                            .date(date)
                            .possibleCount(count)
                            .matchPercentage(percentage)
                            // 이 부분에서 연속된 날짜 등을 계산하여 dateRange를 채울 수 있음 (복잡한 AI 로직)
                            .dateRange(date.format(DATE_FORMATTER))
                            .isRecommended(percentage == 100) // 100% 매칭 시 추천으로 가정
                            .build();
                })
                // 100% 매칭, 카운트 높은 순서, 날짜 순으로 정렬 (AI 추천 기준)
                .sorted(Comparator
                        .comparing(DateMatchInfo::getMatchPercentage).reversed()
                        .thenComparing(DateMatchInfo::getPossibleCount).reversed()
                        .thenComparing(DateMatchInfo::getDate))
                .collect(Collectors.toList());

        // 5. 멤버별 가능한 날짜 목록 (Page 27 하단)
        Map<User, List<AvailableDate>> userDateMap = allAvailableDates.stream()
                .collect(Collectors.groupingBy(AvailableDate::getUser));

        List<DateStatusResponse.MemberPossibility> memberPossibilities = userDateMap.entrySet().stream()
                .map(entry -> DateStatusResponse.MemberPossibility.builder()
                        .userId(entry.getKey().getId())
                        .userName(entry.getKey().getName())
                        .possibleDates(entry.getValue().stream()
                                .map(AvailableDate::getPossibleDate)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return DateStatusResponse.builder()
                .totalMemberCount(totalMemberCount)
                .aiRecommendations(allMatchInfos)
                .memberPossibilities(memberPossibilities)
                .build();
    }

    // API 8: 여행 날짜 확정 (PUT /api/trips/{tripId}/date-confirm)
    @Transactional
    public void confirmDate(Long userId, Long tripId, DateConfirmRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        // 1. 권한 확인 (방장만 확정 가능)
        if (!trip.getCreatorId().equals(userId)) {
            throw new RuntimeException("여행 날짜를 확정할 권한이 없습니다. (방장만 가능)");
        }

        // 2. Trip 엔티티 업데이트
        tripRepository.save(trip.toBuilder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .progress(20) // 날짜 확정 완료 시 진행률 20%로 업데이트 가정
                .build());

        // 참고: Trip 엔티티에 toBuilder() 메서드를 추가해야 합니다.
        // Trip.java 파일에 @Builder(toBuilder = true)를 클래스 레벨에 추가해야 합니다.
    }

    // API 9: 장소 후보지 제안 (POST /api/trips/{tripId}/candidates)
    @Transactional
    public void suggestCandidate(Long userId, Long tripId, CandidateSuggestRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 후보지 엔티티 생성 및 저장
        Candidate candidate = Candidate.builder()
                .trip(trip)
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .suggestedBy(user)
                .isAiRecommended(false) // 사용자가 제안한 것이므로 AI 추천은 false
                .build();

        candidateRepository.save(candidate);
    }

    // API 10: 후보지 목록 조회 (GET /api/trips/{tripId}/candidates)
    @Transactional(readOnly = true)
    public List<CandidateListResponse> getCandidates(Long userId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 1. 해당 여행의 모든 후보지 조회 (투표 수 내림차순 정렬)
        List<Candidate> candidates = candidateRepository.findByTrip(trip);

        // 2. 현재 사용자가 투표한 후보지 목록 조회 (N+1 방지 및 효율적 조회를 위해 별도 조회)
        // Set<Long> votedCandidateIds = candidateVoteRepository.findByUserAndCandidate_Trip(user, trip).stream()
        //         .map(v -> v.getCandidate().getId())
        //         .collect(Collectors.toSet());

        // 임시로 투표 여부 확인을 위해 CandidateVoteRepository의 기본 findByCandidateAndUser 메서드 사용

        // 3. DTO 변환
        return candidates.stream()
                .sorted(Comparator.comparing(Candidate::getVoteCount).reversed()) // 투표 수 기준 내림차순 정렬
                .map(candidate -> {
                    // 현재 사용자의 투표 여부 확인
                    boolean isVotedByMe = candidateVoteRepository.findByCandidateAndUser(candidate, user).isPresent();

                    return CandidateListResponse.builder()
                            .candidateId(candidate.getId())
                            .name(candidate.getName())
                            .category(candidate.getCategory())
                            .description(candidate.getDescription())
                            .suggestedByName(candidate.getSuggestedBy().getName() + "님 제안")
                            .voteCount(candidate.getVoteCount())
                            .isVotedByMe(isVotedByMe)
                            .isItineraryAdded(candidate.isItineraryAdded())
                            .isAiRecommended(candidate.isAiRecommended())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // API 11: 투표 토글 (POST /api/candidates/{candidateId}/vote)
    @Transactional
    public void toggleVote(Long userId, Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("후보지를 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 1. 기존 투표 기록 확인
        Optional<CandidateVote> existingVote = candidateVoteRepository.findByCandidateAndUser(candidate, user);

        if (existingVote.isPresent()) {
            // 2. 투표가 있으면 삭제 (투표 취소)
            candidateVoteRepository.delete(existingVote.get());
            candidate.decrementVoteCount(); // 투표 수 감소
        } else {
            // 3. 투표가 없으면 생성 (투표)
            CandidateVote newVote = CandidateVote.builder()
                    .candidate(candidate)
                    .user(user)
                    .build();
            candidateVoteRepository.save(newVote);
            candidate.incrementVoteCount(); // 투표 수 증가
        }

        // 4. Candidate 엔티티 업데이트 (투표 수 변경 반영)
        candidateRepository.save(candidate);
    }

    // API 12: 일정표 전체 조회 (GET /api/trips/{tripId}/itinerary)
    @Transactional(readOnly = true)
    public ItineraryListResponse getItinerary(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        // Repository에서 날짜/시간 순으로 정렬된 목록을 가져옴
        List<ItineraryItem> items = itineraryItemRepository.findByTripOrderByScheduleDateAscStartTimeAsc(trip);

        // Day(LocalDate)별로 일정 항목을 그룹화
        Map<LocalDate, List<ItineraryItemDetail>> dailyItineraries = items.stream()
                .collect(Collectors.groupingBy(
                        ItineraryItem::getScheduleDate,
                        Collectors.mapping(
                                item -> ItineraryItemDetail.builder()
                                        .itemId(item.getId())
                                        .title(item.getTitle())
                                        .placeName(item.getPlaceName())
                                        .startTime(item.getStartTime())
                                        .estimatedDuration(item.getEstimatedDuration())
                                        .memo(item.getMemo())
                                        .lastEditorId(item.getLastEditorId())
                                        .orderIndex(item.getOrderIndex())
                                        .build(),
                                Collectors.toList()
                        )
                ));

        return ItineraryListResponse.builder()
                .dailyItineraries(dailyItineraries)
                .build();
    }


    // API 13: 새 일정 추가 (POST /api/trips/{tripId}/itinerary)
    @Transactional
    public Long createItineraryItem(Long userId, Long tripId, ItineraryItemRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        // 현재 Day의 최대 순서를 찾아 +1 하여 새로운 순서 부여 (orderIndex 자동 결정)
        // (간소화를 위해 이 로직은 생략하고, 요청받은 orderIndex를 그대로 사용하거나,
        //  요청이 없을 경우 0으로 가정합니다. 실제로는 DB 쿼리를 통해 max(orderIndex)를 가져와야 합니다.)
        int newOrderIndex = request.getOrderIndex() != null ? request.getOrderIndex() : 0;

        ItineraryItem item = ItineraryItem.builder()
                .trip(trip)
                .title(request.getTitle())
                .placeName(request.getPlaceName())
                .scheduleDate(request.getScheduleDate())
                .startTime(request.getStartTime())
                .estimatedDuration(request.getEstimatedDuration())
                .memo(request.getMemo())
                .lastEditorId(userId)
                .orderIndex(newOrderIndex)
                .build();

        return itineraryItemRepository.save(item).getId();
    }


    // API 14: 일정 수정 (PUT /api/itinerary/{itemId})
    @Transactional
    public void updateItineraryItem(Long userId, Long itemId, ItineraryItemRequest request) {
        ItineraryItem existingItem = itineraryItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("수정할 일정을 찾을 수 없습니다."));

        // ItineraryItem 엔티티의 updateBuilder를 사용하여 수정
        ItineraryItem updatedItem = existingItem.updateBuilder()
                .title(request.getTitle())
                .placeName(request.getPlaceName())
                .startTime(request.getStartTime())
                .estimatedDuration(request.getEstimatedDuration())
                .memo(request.getMemo())
                .lastEditorId(userId) // 최종 편집자 업데이트
                .orderIndex(request.getOrderIndex() != null ? request.getOrderIndex() : existingItem.getOrderIndex())
                .updateBuild(); // ItineraryItem의 Builder 메서드 이름과 일치해야 함 (updateBuild)

        itineraryItemRepository.save(updatedItem);
    }

    // API 15: 일정 삭제 (DELETE /api/itinerary/{itemId})
    @Transactional
    public void deleteItineraryItem(Long itemId) {
        // 존재하지 않는 일정 삭제 시 예외 발생
        if (!itineraryItemRepository.existsById(itemId)) {
            throw new ResourceNotFoundException("삭제할 일정을 찾을 수 없습니다.");
        }
        itineraryItemRepository.deleteById(itemId);
    }

    // API 16: 지출 기록 추가 (POST /api/trips/{tripId}/expenses) - Page 47
    @Transactional
    public Long createExpense(Long tripId, ExpenseRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        User payer = userRepository.findById(request.getPayerId())
                .orElseThrow(() -> new ResourceNotFoundException("결제자를 찾을 수 없습니다."));

        Expense expense = Expense.builder()
                .trip(trip)
                .content(request.getContent())
                .amount(request.getAmount())
                .memo(request.getMemo())
                .type(request.getType())
                .payer(payer)
                .expenseDate(LocalDateTime.now())
                .isSettled(false)
                .build();

        return expenseRepository.save(expense).getId();
    }

    // API 17: 예산 현황 조회 (GET /api/trips/{tripId}/budget) - Page 42, 44
    @Transactional(readOnly = true)
    public BudgetStatusResponse getBudgetStatus(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        // 1. 전체 예산 계산
        Long totalBudget = personalBudgetRepository.calculateTotalBudgetAmountByTrip(trip);
        Long totalUsed = expenseRepository.calculateTotalUsedAmountByTrip(trip);
        Long totalRemaining = (totalBudget != null ? totalBudget : 0L) - (totalUsed != null ? totalUsed : 0L);
        Double usagePercentage = totalBudget != null && totalBudget > 0
                ? (double) totalUsed / totalBudget * 100
                : 0.0;

        // 2. 지출 기록 목록
        List<ExpenseDetail> expenseRecords = expenseRepository.findByTripOrderByExpenseDateDesc(trip).stream()
                .map(e -> ExpenseDetail.builder()
                        .expenseId(e.getId())
                        .content(e.getContent())
                        .amount(e.getAmount())
                        .type(e.getType().name())
                        .memo(e.getMemo())
                        .payerName(e.getPayer().getName() + "님 결제")
                        .isSettled(e.isSettled())
                        .build())
                .collect(Collectors.toList());

        // 3. 개인별 예산 현황
        List<PersonalBudgetDetail> personalBudgets = personalBudgetRepository.findByTrip(trip).stream()
                .map(pb -> PersonalBudgetDetail.builder()
                        .userId(pb.getUser().getId())
                        .userName(pb.getUser().getName())
                        .budgetAmount(pb.getBudgetAmount())
                        // 실제 사용 금액 계산 로직은 복잡하여 일단 0%로 가정
                        .usagePercentage(0.0)
                        .build())
                .collect(Collectors.toList());

        // 4. 정산 대기 현황 (공용 지출만)
        List<Expense> unsettledShared = expenseRepository.findByTripAndTypeAndIsSettledFalse(trip, ExpenseType.SHARED);
        Long unsettledSharedAmount = unsettledShared.stream().mapToLong(Expense::getAmount).sum();
        int memberCount = tripMemberRepository.countByTrip(trip);
        Long perPersonShare = memberCount > 0 ? unsettledSharedAmount / memberCount : 0L;

        return BudgetStatusResponse.builder()
                .totalBudget(totalBudget)
                .totalUsed(totalUsed)
                .totalRemaining(totalRemaining)
                .usagePercentage(usagePercentage)
                .expenseRecords(expenseRecords)
                .personalBudgets(personalBudgets)
                .unsettledSharedAmount(unsettledSharedAmount)
                .unsettledCount(unsettledShared.size())
                .perPersonShare(perPersonShare)
                .build();
    }

    // API 18: 내 예산 추가/수정 (POST /api/trips/{tripId}/personal-budget) - Page 45
    @Transactional
    public void updatePersonalBudget(Long userId, Long tripId, PersonalBudgetRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 기존 예산이 있는지 확인 (있으면 수정, 없으면 생성)
        Optional<PersonalBudget> existingBudget = personalBudgetRepository.findByTripAndUser(trip, user);

        if (existingBudget.isPresent()) {
            existingBudget.get().updateBudget(request.getBudgetAmount());
        } else {
            PersonalBudget newBudget = PersonalBudget.builder()
                    .trip(trip)
                    .user(user)
                    .budgetAmount(request.getBudgetAmount())
                    .build();
            personalBudgetRepository.save(newBudget);
        }
    }

    // API 19: 정산 완료 (POST /api/trips/{tripId}/settlement) - Page 48
    @Transactional
    public List<ExpenseDetail> completeSettlement(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));

        // 정산 대상: 정산되지 않은 모든 공용 지출
        List<Expense> expensesToSettle = expenseRepository.findByTripAndTypeAndIsSettledFalse(trip, ExpenseType.SHARED);

        // isSettled 상태를 true로 변경
        expensesToSettle.forEach(expense -> expense.toBuilder().isSettled(true).build()); // Expense 엔티티에 toBuilder() 필요
        expenseRepository.saveAll(expensesToSettle);

        // 정산 완료된 목록을 DTO로 변환하여 반환 (응답에 정산 결과를 포함할 수 있음)
        return expensesToSettle.stream()
                .map(e -> ExpenseDetail.builder()
                        .expenseId(e.getId())
                        .content(e.getContent())
                        .amount(e.getAmount())
                        .type(e.getType().name())
                        .memo(e.getMemo())
                        .payerName(e.getPayer().getName() + "님 결제")
                        .isSettled(true)
                        .build())
                .collect(Collectors.toList());
    }

    // API 20: 체크리스트 목록 조회 (GET /api/trips/{tripId}/checklists) - Page 50
    @Transactional(readOnly = true)
    public ChecklistListResponse getChecklists(Long userId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 1. 공용 체크리스트 조회 (모두에게 보임)
        List<ChecklistItem> sharedItems = checklistItemRepository.findByTripAndType(trip, ChecklistType.SHARED);

        // 2. 개인 체크리스트 조회 (현재 사용자에게만 보임)
        List<ChecklistItem> personalItems = checklistItemRepository.findByTripAndOwnerUser_IdAndType(
                trip, currentUser.getId(), ChecklistType.PERSONAL);

        // 3. DTO 변환
        List<ChecklistDetail> sharedChecklists = convertChecklistToDetail(sharedItems);
        List<ChecklistDetail> personalChecklists = convertChecklistToDetail(personalItems);

        return ChecklistListResponse.builder()
                .sharedChecklists(sharedChecklists)
                .personalChecklists(personalChecklists)
                .build();
    }

    // DTO 변환 헬퍼 메서드
    private List<ChecklistDetail> convertChecklistToDetail(List<ChecklistItem> items) {
        return items.stream()
                .sorted(Comparator.comparing(ChecklistItem::isCompleted)) // 미완료(false)가 위로 오도록 정렬
                .map(item -> ChecklistDetail.builder()
                        .itemId(item.getId())
                        .content(item.getContent())
                        .description(item.getDescription())
                        .assignedUserName(item.getAssignedUser() != null ? item.getAssignedUser().getName() : null)
                        .ownerUserName(item.getOwnerUser() != null ? item.getOwnerUser().getName() : null)
                        .dueDate(item.getDueDate())
                        .isCompleted(item.isCompleted())
                        .build())
                .collect(Collectors.toList());
    }


    // API 21: 체크리스트 항목 추가 (POST /api/trips/{tripId}/checklists) - Page 52, 54
    @Transactional
    public Long createChecklistItem(Long userId, Long tripId, ChecklistRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("여행 방을 찾을 수 없습니다."));
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        User assignedUser = null;
        // 공용일 경우 담당자 조회
        if (request.getType() == ChecklistType.SHARED && request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("담당자를 찾을 수 없습니다."));
        }

        ChecklistItem item = ChecklistItem.builder()
                .trip(trip)
                .type(request.getType())
                .content(request.getContent())
                .description(request.getDescription())
                .assignedUser(assignedUser) // 공용일 경우 담당자 지정
                .ownerUser(request.getType() == ChecklistType.PERSONAL ? currentUser : null) // 개인일 경우 소유자 지정
                .dueDate(request.getDueDate())
                .isCompleted(false)
                .build();

        return checklistItemRepository.save(item).getId();
    }

    // API 22: 체크리스트 완료 상태 토글 (PUT /api/checklists/{itemId}/toggle) - Page 50, 58
    @Transactional
    public void toggleChecklistCompletion(Long itemId) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("체크리스트 항목을 찾을 수 없습니다."));

        // 상태 토글
        item.toggleCompletion();

        // 일정표 상태에 따라 여행 진행률 업데이트 로직을 추가할 수 있음
        // tripRepository.save(item.getTrip().toBuilder().progress(newProgress).build());
    }

    // API 23: 체크리스트 항목 삭제 (DELETE /api/checklists/{itemId})
    @Transactional
    public void deleteChecklistItem(Long itemId) {
        if (!checklistItemRepository.existsById(itemId)) {
            throw new ResourceNotFoundException("삭제할 체크리스트 항목을 찾을 수 없습니다.");
        }
        checklistItemRepository.deleteById(itemId);
    }
}