<!-- 灾害模拟业务流程 -->
<template>
  <div class="app-container">
    <!-- 步骤组件 -->
    <div class="step-container">
      <stepA @onChangeCity="onChangeCity" v-show="stepIndex == 0" />
      <stepB :city="city" :cityName="cityName" @onChangeTask="onChangeTask" @onChangeTaskList="onChangeTaskList"
             v-if="stepIndex == 1" />
      <stepC :city="city" :cityName="cityName" :task="task" :taskList="infectionSimulationList" v-if="stepIndex == 2"
             @onChangeRisk="onChangeRisk"/>
      <stepD :city="city" :cityName="cityName" :task="task" :taskId="taskId" v-if="stepIndex === 3"
             @onChangeItems="onChangeItems" />
      <stepE ref="editJointPlan" :city="city" :cityName="cityName" :task="task" :taskId="taskId"
             :items="items"
             :ChosedItem="selectedItems"
             :riskSource="riskSource"
             v-if="stepIndex === 4" />
    </div>
    <!-- 切换步骤按钮 -->
    <div class="step-button-container">
      <el-button size="large" @click="onPreStep" :disabled="disabledPre">上一步</el-button>
      <el-button type="primary" size="large" @click="onNextStep" :disabled="disabledNext">下一步</el-button>

    </div>
  </div>
</template>

<script>
// 导入步骤组件
import stepA from "./stepA.vue";
import stepB from "./stepB.vue";
import stepC from "./stepC.vue";

import mapData from '@/views/simulator/components/mapData';   //导入城市名数据

export default {
  name: "simulation",
  props: [""],
  components: {
    stepA, stepB, stepC
  },
  data() {
    return {
      stepIndex: 0,     // 当前步骤索引
      maxStepIndex: 4,  // 最大步骤索引
      city: "",         // 选择的城市
      cityName: "",     // 选择的城市名
      taskId: -1,       // 选择的任务ID
      task: null,       // 选择的任务
      infectionSimulationList: [], //传染病模拟任务列表
      items: null,
      selectedItems: null,
      riskSource: null,
    };
  },
  computed: {
    disabledPre: function ()   // 上一步按钮是否禁用
    {
      if (this.stepIndex <= 0)
        return true;
      return false;
    },
    disabledNext: function ()  // 下一步按钮是否禁用
    {
      if (this.stepIndex >= this.maxStepIndex)
        return true;
      switch (this.stepIndex) {
        case 0:
          if (this.city === "") return true;
          break;
        case 1:
          if (this.taskId === -1) return true;
          break;
        case 2:
          if (this.riskSource === null) return true;
          break;
        default:
          return false;
      }
    },
  },
  mounted() {
  },
  methods: {
    // 切换到下一步骤
    onNextStep() {
      this.stepIndex++;
    },
    // 切换到上一步骤
    onPreStep() {
      this.stepIndex--;
    },
    // 选择城市
    onChangeCity(city) {
      this.city = city;
      this.cityName = this.getCityName(city);
    },
    // 选择模拟推演任务
    onChangeTask(task) {
      this.task = task;
      this.taskId = task.id;
    },
    // 根据拼音获取城市名
    getCityName(city) {
      if (city in mapData) {
        return mapData[city].cityName
      } else {
        return city
      }
    },
    onChangeTaskList(tasks) {
      this.infectionSimulationList = tasks;
    },
    onChangeItems(items, chooseItems) {
      this.items = items;
      console.log("chooseItems", chooseItems)
      this.selectedItems = chooseItems;
    },
    onChangeRisk(riskSource){
      this.riskSource = riskSource;
    }
  },
};
</script>
<style lang="scss" scoped>
.app-container {
  width: 100%;
  padding: 20px;

  .step-container {
    width: 100%;
  }

  .step-button-container {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  }

}
</style>
