package com.ruoyi.infection.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.infection.domain.LockSimulationTimeRecord;
import com.ruoyi.infection.domain.SimulationRecord;
import java.util.Map;

public interface LockSimulationTimeRecordMapper {
    Integer selectMaxId(String userId);
    void insertLockSimulationTimeRecord(LockSimulationTimeRecord record);
    Map<String, String> getLockSimulationTimeByTime(@Param("time") String time,@Param("userId") String userId);

    Map<String, String> getLatestLockSimulationTime(@Param("city") String city,@Param("userId") String userId);

    Map<String, String> getMaddpgSimulationTimeByTime(@Param("time") String time,@Param("userId")String userId);

    Map<String, String> getLatestMaddpgSimulationTime(@Param("city") String city,@Param("userId") String userId);
    Integer findLatestSimulationId(@Param("city") String city,@Param("userId") String userId);
    String findFilePathById(@Param("id") Integer id,@Param("userId") String userId);
    Integer findLockLatestSimulationId(@Param("city") String city,@Param("userId") String userId);
    String findLockFilePathById(@Param("id") Integer id,@Param("userId") String userId);
    Long getLatestSimulationId(String userId);

    String getPolicyFilePathBySimulationId(@Param("simulationId") Long simulationId);

    Long getSimulationIdByFilePath(@Param("filePath") String filePath,@Param("userId") String userId);
    Long getLatestRecordId(@Param("city")String city,@Param("userId") String userId);

    String getFilepathById(Long id);
}