package com.ruoyi.infection.service.impl;

import com.alibaba.fastjson2.support.csv.CSVReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruoyi.infection.domain.SimulationRequest;
import com.ruoyi.infection.mapper.LockSimulationRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.infection.service.ILockSimulationService;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.nio.file.Files;

@Service
public class LockSimulationServiceImpl implements ILockSimulationService {

    @Autowired
    private LockSimulationRecordMapper lockSimulationRecordMapper;
    private static final String ROOT_FILE_PATH = System.getProperty("user.dir") + "\\testUser\\";

    @Override
    public Map<String, Object> getLockEveryHourInfection(String city,String userId, String simulationFileName) {
        String dir = ROOT_FILE_PATH+userId + "\\" + "SimulationResult" +  "\\"+"lock_result\\"+city + "\\";
        if (Objects.equals(simulationFileName, "latestRecord")) {
            List<Long> ids = lockSimulationRecordMapper.selectIdsByCity(city,userId);
            Long maxId = ids.stream().max(Long::compare).orElse(-1L);

            if (maxId == -1) {
                return null; // No records
            }

            String queryFilePath = lockSimulationRecordMapper.selectFilepathById(maxId);
            dir = dir+queryFilePath+"\\";
        } else {
            dir = dir+simulationFileName+"\\";
        }

        // Load data from CSV files
        List<Double> result = new ArrayList<>();// 用于存储每个文件中 "I" 列的总和
        int curHour = 0;
        while (new File(dir + "simulation_DSIHR_result_" + curHour + ".csv").exists()) {
            double sum = loadAndSumInfections(dir + "simulation_DSIHR_result_" + curHour + ".csv");
            result.add(sum*0.8 );
            curHour++;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);

        return response;
    }

    @Override
    public Map<String, Object> getEveryHourInfection(String city, String userId,String simulationFileName) {
        String dir = ROOT_FILE_PATH+userId + "\\" + "SimulationResult" +  "\\"+"unlock_result\\"+city + "\\";
        if (Objects.equals(simulationFileName, "latestRecord")) {
            List<Long> ids = lockSimulationRecordMapper.selectIdByCity(city,userId);
            Long maxId = ids.stream().max(Long::compare).orElse(-1L);

            if (maxId == -1) {
                return null; // No records
            }

            String queryFilePath = lockSimulationRecordMapper.selectFilespathById(maxId);
            dir = dir+queryFilePath+"\\";
        } else {
            dir = dir+simulationFileName+"\\";
        }

        // Load data from CSV files
        List<Double> result = new ArrayList<>();
        int curHour = 0;
        System.out.println("文件路径："+dir + "simulation_DSIHR_result_" + curHour + ".csv");
        while (new File(dir + "simulation_DSIHR_result_" + curHour + ".csv").exists()) {
            System.out.println("文件存在！");
            double sum = loadAndSumInfections(dir + "simulation_DSIHR_result_" + curHour + ".csv");

            curHour++;
            result.add(sum * 0.8);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("result", result);

        return response;
    }
    @Override
    public Map<String, Object> getMADDPGEveryHourInfection(String city, String userId,String simulationFileName) {
        String dir = ROOT_FILE_PATH+userId + "\\" + "SimulationResult" +  "\\"+"MADDPG_result\\"+city + "\\";
        if (Objects.equals(simulationFileName, "latestRecord")) {
            List<Long> ids = lockSimulationRecordMapper.selectMADDPGIdByCity(city,userId);
            Long maxId = ids.stream().max(Long::compare).orElse(-1L);

            if (maxId == -1) {
                return null; // No records
            }

            String queryFilePath = lockSimulationRecordMapper.selectMADDPGFilespathById(maxId);
            dir = dir+queryFilePath+"\\";
        } else {
            dir = dir+simulationFileName+"\\";
        }

        // Load data from CSV files
        List<Double> result = new ArrayList<>();
        int curHour = 0;
        // 循环，直到没有找到文件
        while (true) {
            File file = new File(dir + "simulation_DSIHR_result_" + curHour + ".csv");

            // 检查文件是否存在
            if (!file.exists()) {
                break;
            }
            try (Reader reader = new FileReader(file)) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader("Column1","geometry", "S", "I", "H", "R", "new_infected", "total_num") // 为所有列指定名称
                .withSkipHeaderRecord(true) // 表头仍然作为数据解析的依据
                .withTrim()
                .parse(reader);
                double sumColumn = 0.0;
            // 遍历每一行，累加特定列值
            for (CSVRecord record : records) {
                try {
                    double value = Double.parseDouble(record.get("I")); // 假设目标列是索引2
                    sumColumn += value;
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid number in file: " + file);
                }
            }
                // 将结果添加到列表
                result.add(sumColumn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            curHour++;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);

        return response;
    }



    private double loadAndSumInfections(String filePath) {
        double totalInfections=0.0;
        try (Reader reader = new FileReader(filePath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
            .withHeader("Column1","geometry", "S", "I", "H", "R", "new_infected", "total_num") // 为所有列指定名称
            .withSkipHeaderRecord(true) // 表头仍然作为数据解析的依据
            .withTrim()
            .parse(reader);

        // 遍历每一行，累加特定列值
        for (CSVRecord record : records) {
            try {
                double value = Double.parseDouble(record.get("I")); // 假设目标列是索引2
                totalInfections += value;
            } catch (NumberFormatException e) {
                System.err.println("Skipping invalid number in file: " + filePath);
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return totalInfections;
    }

 @Override
    public Map<String, Object> getMADDPGRiskPoints(String userId,String simulationCity,int simulationDay,int simulationHour,int thresholdInfected,String simulationFileName ) {
        Map<String, Object> response = new HashMap<>();


        if (simulationFileName == null || simulationFileName.isEmpty()) {
            simulationFileName = "latestRecord";
        }

        String baseDir =  ROOT_FILE_PATH+userId + "\\" + "SimulationResult" +  "\\"+"MADDPG_result\\"+simulationCity + "\\";
        if ("latestRecord".equals(simulationFileName)) {
            Integer curId = lockSimulationRecordMapper.getLatestSimulationId(simulationCity,userId);
            if (curId == null) {
                response.put("msg", "没有最新的模拟记录");
                return response;
            }
            String queryFileName = lockSimulationRecordMapper.getFilePathById(curId);
            baseDir += queryFileName + "\\";
        } else {
            baseDir += simulationFileName + "\\";
        }

        simulationHour = (simulationDay - 1) * 24 + (simulationHour - 1);
        String filePath = baseDir + "simulation_DSIHR_result_" + simulationHour + ".csv";

        File file = new File(filePath);
        if (!file.exists()) {
            response.put("msg", "没有这个时刻的模拟结果");
            return response;
        }

        List<List<Object>> result = new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
            .withHeader("Column1","geometry", "S", "I", "H", "R", "new_infected", "total_num") // 为所有列指定名称
            .withSkipHeaderRecord(true) // 表头仍然作为数据解析的依据
            .withTrim()
            .parse(reader);
            int index = 0;
            for (CSVRecord record : records) {
                try {
                    // 假设目标列是索引 2 (感染人数)
                    double infectedCount = Double.parseDouble(record.get(2));
                    if (infectedCount >= thresholdInfected) {
                        // 将索引和感染人数存入 result，符合定义 List<List<Object>>
                        List<Object> entry = new ArrayList<>();
                        entry.add(index); // 索引
                        entry.add(infectedCount); // 感染人数
                        result.add(entry);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid number in file: " + filePath);
                }
                index++;
            }
            if (result.isEmpty()) {
                response.put("msg", "没有超过阈值的网格");
            } else {
                response.put("msg", "success");
            }
        } catch (Exception e) {
            response.put("msg", "读取 .csv 文件失败");
            return response;
        }
        response.put("result", result);
        return response;
    }






}