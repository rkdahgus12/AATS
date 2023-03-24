package com.ssafy.d102.service.impl;

import com.ssafy.d102.data.Exception.NoContentException;
import com.ssafy.d102.data.Exception.NotMatchException;
import com.ssafy.d102.data.dto.*;
import com.ssafy.d102.data.entity.*;
import com.ssafy.d102.repository.OrganizationRepository;
import com.ssafy.d102.repository.WorkerAttendanceEndRepository;
import com.ssafy.d102.repository.WorkerAttendanceStartRepository;
import com.ssafy.d102.repository.WorkerRepository;
import com.ssafy.d102.service.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationRepository organizationRepository;
    private final WorkerAttendanceStartRepository workerAttendanceStartRepository;
    private final WorkerAttendanceEndRepository workerAttendanceEndRepository;

//    public WorkerServiceImpl(WorkerRepository repository, PasswordEncoder passwordEncoder, OrganizationRepository organizationRepository) {
//        this.repository = repository;
//        this.passwordEncoder = passwordEncoder;
//        this.organizationRepository = organizationRepository;
//    }

    @Override
    public WorkerDto loginWorker(WorkerLoginDto input) {
        String id = input.getWorkerId();

        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new NoContentException("Id를 확인해주세요."));

        if(!passwordEncoder.matches(input.getWorkerPwd(), worker.getWorkerPw())) {
            throw new NotMatchException("PW가 다릅니다.");
        }

        return WorkerDto.builder()
                .workerId(worker.getWorkerId())
                .workerName(worker.getWorkerName())
                .workerStatus(worker.getWorkerStatus())
                .workerOrganizationId(worker.getOrganization().getOrganizationId())
                .workerGender(worker.getWorkerGender())
                .workerAge(worker.getWorkerAge())
                .workerPhone(worker.getWorkerPhone())
                .workerEmail(worker.getWorkerEmail())
                .workerBirth(worker.getWorkerBirth())
                .workerNationality(worker.getWorkerNationality())
                .workerProfile(null)
                .workerRegistDate(worker.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))
                .workerUpdateDate(worker.getUpdated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))
                .build();
    }

    @Override
    public List<WorkerDto> getAllWorker() {
        List<WorkerDto> list = new ArrayList<>();
        List<Worker> workerlist = new ArrayList<>();

        for (Worker worker : workerRepository.findAll()) {
            list.add(new WorkerDto(
                    worker.getWorkerId(),
                    worker.getWorkerName(),
                    worker.getWorkerStatus(),
                    worker.getOrganization().getOrganizationId(),
                    worker.getWorkerGender(),
                    worker.getWorkerAge(),
                    worker.getWorkerPhone(),
                    worker.getWorkerEmail(),
                    worker.getWorkerBirth(),
                    worker.getWorkerNationality(),
                    null,
                    worker.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                    worker.getUpdated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            ));
        }

        return list;
    }

    @Override
    public WorkerDto getWorkerById(String id) {
        Optional<Worker> worker = workerRepository.findById(id);

        if (worker.isPresent()) {
            return new WorkerDto(
                    worker.get().getWorkerId(),
                    worker.get().getWorkerName(),
                    worker.get().getWorkerStatus(),
                    worker.get().getOrganization().getOrganizationId(),
                    worker.get().getWorkerGender(),
                    worker.get().getWorkerAge(),
                    worker.get().getWorkerPhone(),
                    worker.get().getWorkerEmail(),
                    worker.get().getWorkerBirth(),
                    worker.get().getWorkerNationality(),
                    null,
                    worker.get().getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                    worker.get().getUpdated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            );
        } else {
            return null;
        }
    }

    @Override
    public void updateWorker(WorkerRegistDto input) {
        Worker worker = workerRepository.findById(input.getWorkerId())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 없습니다"));

        workerRepository.save(worker.builder()
                .workerId(worker.getWorkerId())
                .workerPw(passwordEncoder.encode(input.getWorkerPwd()))
                .workerName(input.getWorkerName())
                .workerStatus(worker.getWorkerStatus())
                .organization(organizationRepository.findById(input.getWorkerOrganizationId()).get())
                .workerGender(input.getWorkerGender())
                .workerAge(input.getWorkerAge())
                .workerPhone(input.getWorkerPhone())
                .workerEmail(input.getWorkerEmail())
                .workerBirth(input.getWorkerBirth())
                .workerNationality(input.getWorkerNationality())
                .build());
    }

    @Override
    public void updateWorkerPw(WorkerUpdatePwDto input) {
        Worker worker = workerRepository.findById(input.getWorkerId())
                .orElseThrow(() -> new NoContentException("입력하신 아이디가 없습니다."));

        if(!passwordEncoder.matches(input.getWorkerPwd(),worker.getWorkerPw()))
            throw new NotMatchException("입력하신 비밀번호가 다릅니다.");


        workerRepository.save(Worker.builder()
                .workerId(worker.getWorkerId())
                .workerPw(passwordEncoder.encode(input.getWorkerNewPwd()))
                .workerName(worker.getWorkerName())
                .workerStatus(worker.getWorkerStatus())
                .organization(worker.getOrganization())
                .workerGender(worker.getWorkerGender())
                .workerAge(worker.getWorkerAge())
                .workerPhone(worker.getWorkerPhone())
                .workerEmail(worker.getWorkerEmail())
                .workerBirth(worker.getWorkerBirth())
                .workerNationality(worker.getWorkerNationality())
                .build());
    }

    @Override
    public void startWorker(String id) {
        Optional<Worker> worker = workerRepository.findById(id);

        if (worker.isPresent()) {
            workerAttendanceStartRepository.save(
                    WorkerAttendanceStart.builder()
                            .startTime(LocalDateTime.now())
                            .worker(worker.get())
                            .build()
            );
        }
    }

    @Override
    public void endWorker(String id) {
        Optional<Worker> worker = workerRepository.findById(id);

        if (worker.isPresent()) {
            workerAttendanceEndRepository.save(
                    WorkerAttendanceEnd.builder()
                            .endTime(LocalDateTime.now())
                            .worker(worker.get())
                            .build()
            );
        }
    }


    @Override
    public void registWorker(WorkerRegistDto input) {

        Organization organization = organizationRepository.findById(input.getWorkerOrganizationId())
                .orElseThrow(() -> new NoContentException("없는 기관입니다."));


        workerRepository.save(Worker.builder()
                .workerId(input.getWorkerId())
                .workerPw(passwordEncoder.encode(input.getWorkerPwd()))
                .workerName(input.getWorkerName())
                .workerStatus(input.getWorkerStatus())
                .organization(organizationRepository.findById(input.getWorkerOrganizationId()).get())
                .workerGender(input.getWorkerGender())
                .workerAge(input.getWorkerAge())
                .workerPhone(input.getWorkerPhone())
                .workerEmail(input.getWorkerEmail())
                .workerBirth(input.getWorkerBirth())
                .workerNationality(input.getWorkerNationality())
                .workerProfile(new byte[1])
                .build());
    }

    @Override
    public void deleteWorker(String id) {

        Optional<Worker> worker = workerRepository.findById(id);

        if (worker.isPresent()) {
            workerRepository.delete(worker.get());
        }
    }

    @Override
    public List<DateTimeDto> getWorkerStart(String id) {
        List<DateTimeDto> list = new ArrayList<>();
        List<WorkerAttendanceStart> workerStartList =
                workerAttendanceStartRepository.findAll().stream()
                        .filter(workerAttendanceStart -> workerAttendanceStart.getWorker().getWorkerId().equals(id))
                        .collect(Collectors.toList());


        for (WorkerAttendanceStart startList : workerStartList) {
            list.add(new DateTimeDto(
                    startList.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            ));
        }

        return list;
    }

    @Override
    public List<DateTimeDto> getWorkerEnd(String id) {
        List<DateTimeDto> list = new ArrayList<>();
        List<WorkerAttendanceEnd> workerEndList =
                workerAttendanceEndRepository.findAll().stream()
                        .filter(workerAttendanceEnd -> workerAttendanceEnd.getWorker().getWorkerId().equals(id))
                        .collect(Collectors.toList());


        for (WorkerAttendanceEnd endList : workerEndList) {
            list.add(new DateTimeDto(
                    endList.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            ));
        }

        return list;
    }
}
