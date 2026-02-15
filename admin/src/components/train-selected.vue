
<template>
  <a-select v-model:value="trainCode" show-search allowClear
            @change="onChange" placeholder="请选择车次"
            :filter-option="filterTrainCodeOption" :style="'width: ' + _width">
    <a-select-option v-for="item in trains" :key="item.code" :value="item.code" :label="item.code + item.start + item.end">
      {{item.code}} | {{item.start}} ~ {{item.end}}
    </a-select-option>
  </a-select>
</template>

<script>

import {defineComponent, onMounted, ref, watch} from "vue";
import {notification} from "ant-design-vue";
import axios from "axios";

export default defineComponent({
  name: "train-selected-view",
  props: ["modelValue", "width"],
  emits: ['update:modelValue','change'],
  setup: function (props, {emit}) {

    const trainCode = ref();
    const trains = ref([]);
    const _width = ref(props.width);
    if (Tool.isEmpty(props.width)) {
      _width.value = "100%";
    }

    /**
     * 车次下拉框筛选
     */
    const filterTrainCodeOption = (input, option) => {
      console.log(input, option);
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    /**
     * 查询所有车次，用于车次下拉框
     */
    const queryTrainAll = () => {
      axios.get("/business/admin/train/query-all").then((response) => {
        let data = response.data;
        if (data.success) {
          console.log(data.content);
          trains.value = data.content
        } else {
          notification.error({description: data.message});
        }
      });
    };

    /**
     *当前组件从父组件取值用watch, 将当前的值响应给父组件用事件
     * 1.利用监听函数watch，动态获取父组件的值，如果放在onMounted或者其他方法，则只有第一次有效
     */
    watch(() => props.modelValue, () => {
      console.log("父组件的值：", props.modelValue);
      trainCode.value = props.modelValue;
    }, {immediate: true});

    /**
     * 2.利用事件将当前组件的值响应给父组件
     */
    const onChange = (value) => {
      emit("update:modelValue", value);
      let train = trains.value.filter((item) => item.code === value)[0];
      if (Tool.isEmpty(train)) {
        train = {};
      }
      emit("change", train)
    };

    onMounted(() => {
      queryTrainAll();
    });
    return {
      queryTrainAll,
      filterTrainCodeOption,
      trains,
      trainCode,
      onChange,
      _width
    };
  }


})

</script>
