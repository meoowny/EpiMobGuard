package com.ruoyi.web.controller.infection;

import com.ruoyi.infection.service.ILockSimulationService;
import com.ruoyi.infection.domain.SimulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
@RestController
public class LockSimulationController {

    @Autowired
    private ILockSimulationService lockSimulationService;

    @PostMapping("/get_lock_every_hour_infection")
    public Object getLockEveryHourInfection(@RequestParam String city,@RequestParam String userId,
                                            @RequestParam(required = false, defaultValue = "latestRecord") String simulationFileName) {
        Map<String, Object> result = lockSimulationService.getLockEveryHourInfection(city,userId, simulationFileName);
        if (result == null) {
            return "没有最新的模拟记录";
        }
        return result;
    }
    @PostMapping("/get_every_hour_infection")
    public Object getEveryHourInfection(@RequestParam String city,@RequestParam String userId,
                                            @RequestParam(required = false, defaultValue = "latestRecord") String simulationFileName) {
        Map<String, Object> result = lockSimulationService.getEveryHourInfection(city,userId, simulationFileName);
        if (result == null) {
            return "没有最新的模拟记录";
        }
        return result;
    }
    @PostMapping("/get_MADDPG_every_hour_result")
    public Object get_MADDPG_every_hour_result(@RequestParam String city,@RequestParam String userId,
                                            @RequestParam(required = false, defaultValue = "latestRecord") String simulationFileName) {
        Map<String, Object> result = lockSimulationService.getMADDPGEveryHourInfection(city,userId, simulationFileName);
        if (result == null) {
            return "没有最新的模拟记录";
        }
        return result;
    }
    @PostMapping("/get_MADDPG_simulation_risk_point")
    public ResponseEntity<?> getMADDPGRiskPoints(@RequestParam("user_id") String userId,
    @RequestParam("simulation_city") String simulationCity,
    @RequestParam("simulation_day") int simulationDay,
    @RequestParam("simulation_hour") int simulationHour,
    @RequestParam("threshold_infected") int thresholdInfected,
    @RequestParam(value = "simulation_file_name", required = false, defaultValue = "latestRecord") String simulationFileName) {
        Map<String, Object> response = lockSimulationService.getMADDPGRiskPoints(userId,simulationCity, simulationDay,simulationHour,thresholdInfected,simulationFileName);
        return ResponseEntity.ok(response);
    }
}
