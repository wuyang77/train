<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="paramss.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
      <train-selected-view v-model="paramss.trainCode" width="200px"></train-selected-view>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainStations"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
          <a-space>
            <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    @confirm="onDelete(record)"
                    ok-text="确认" cancel-text="取消">
              <a style="color: red">删除</a>
            </a-popconfirm>
            <a @click="onEdit(record)">编辑</a>
          </a-space>
      </template>
    </template>
  </a-table>
    <a-modal v-model:visible="visible" title="每日车站" @ok="handleOk"
             ok-text="确认" cancel-text="取消">
      <a-form :model="dailyTrainStation" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
            <a-form-item label="日期">
              <a-date-picker v-model:value="dailyTrainStation.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
            </a-form-item>
            <a-form-item label="车次编号">
              <train-selected-view v-model="dailyTrainStation.trainCode"></train-selected-view>
            </a-form-item>
            <a-form-item label="站序">
              <a-input v-model:value="dailyTrainStation.index" />
            </a-form-item>
            <a-form-item label="站名">
              <a-input v-model:value="dailyTrainStation.name" />
            </a-form-item>
            <a-form-item label="站名拼音">
              <a-input v-model:value="dailyTrainStation.namePinyin" />
            </a-form-item>
            <a-form-item label="进站时间">
              <a-time-picker v-model:value="dailyTrainStation.inTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
            </a-form-item>
            <a-form-item label="出站时间">
              <a-time-picker v-model:value="dailyTrainStation.outTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
            </a-form-item>
            <a-form-item label="停站时长">
              <a-time-picker v-model:value="dailyTrainStation.stopTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
            </a-form-item>
            <a-form-item label="里程（公里）">
              <a-input v-model:value="dailyTrainStation.km" />
            </a-form-item>
      </a-form>
    </a-modal>
</template>

<script>
import {defineComponent, ref, onMounted, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectedView from "@/components/train-selected.vue";
import {pinyin} from "pinyin-pro";
import dayjs from "dayjs";

  export default defineComponent({
    name: "daily-train-station-view",
    components: {TrainSelectedView},
    setup() {
      const visible = ref(false);
      let dailyTrainStation = ref({
        id: undefined,
        date: undefined,
        trainCode: undefined,
        index: undefined,
        name: undefined,
        namePinyin: undefined,
        inTime: undefined,
        outTime: undefined,
        stopTime: undefined,
        km: undefined,
        createTime: undefined,
        updateTime: undefined,
      });
      const dailyTrainStations = ref([]);
      // 分页的三个属性名是固定的
      const pagination = ref({
        total: 0,
        current: 1,
        pageSize: 5,
      });
      let loading = ref(false);

      let paramss = ref({
        trainCode: null,
        date: null
      });
      const columns = [
        {
          title: '日期',
          dataIndex: 'date',
          key: 'date',
        },
        {
          title: '车次编号',
          dataIndex: 'trainCode',
          key: 'trainCode',
        },
        {
          title: '站序',
          dataIndex: 'index',
          key: 'index',
        },
        {
          title: '站名',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '站名拼音',
          dataIndex: 'namePinyin',
          key: 'namePinyin',
        },
        {
          title: '进站时间',
          dataIndex: 'inTime',
          key: 'inTime',
        },
        {
          title: '出站时间',
          dataIndex: 'outTime',
          key: 'outTime',
        },
        {
          title: '停站时长',
          dataIndex: 'stopTime',
          key: 'stopTime',
        },
        {
          title: '里程（公里）',
          dataIndex: 'km',
          key: 'km',
        },
        {
          title: '操作',
          dataIndex: 'operation'
        }
      ];

      watch(() => dailyTrainStation.value.name, () => {
        if (Tool.isNotEmpty(dailyTrainStation.value.name)) {
          dailyTrainStation.value.namePinyin = pinyin(dailyTrainStation.value.name, {toneType: 'none'}).replaceAll(" ", "");
        } else {
          dailyTrainStation.value.namePinyin = "";
        }
      });

      //进站时间和出站时间两者之中任意一个发生变化，都需要对齐进行监听并进行差值和格式处理
      //自动计算停车时长
      watch(() => dailyTrainStation.value.outTime, () => {
        let diff = dayjs(dailyTrainStation.value.outTime, "HH:mm:ss").diff(dayjs(dailyTrainStation.value.inTime, "HH:mm:ss"), "second");
        dailyTrainStation.value.stopTime = dayjs('00:00:00', 'HH:mm:ss').second(diff).format("HH:mm:ss");
      }, {immediate: true});
      //自动计算停车时长

      const onAdd = () => {
        dailyTrainStation.value = {};
        visible.value = true;
      };

      const onEdit = (record) => {
        dailyTrainStation.value = window.Tool.copy(record);
        visible.value = true;
      };

      const onDelete = (record) => {
        axios.delete("/business/admin/daily-train-station/delete/" + record.id).then((response) => {
          const data = response.data;
          if (data.success) {
            notification.success({description: "删除成功！"});
            handleQuery({
              pageNum: pagination.value.current,
              pageSize: pagination.value.pageSize,
            });
          } else {
            notification.error({description: data.message});
          }
        });
      };

      const handleOk = () => {
        axios.post("/business/admin/daily-train-station/save", dailyTrainStation.value).then((response) => {
          let data = response.data;
          if (data.success) {
            notification.success({description: "保存成功！"});
            visible.value = false;
            handleQuery({
              pageNum: pagination.value.current,
              pageSize: pagination.value.pageSize
            });
          } else {
            notification.error({description: data.message});
          }
        });
      };

      const handleQuery = (param) => {
        if (!param) {
          param = {
            pageNum: 1,
            pageSize: pagination.value.pageSize
          };
        }
        loading.value = true;
        axios.get("/business/admin/daily-train-station/query-list", {
          params: {
            pageNum: param.pageNum,
            pageSize: param.pageSize,
            date: paramss.value.date,
            trainCode: paramss.value.trainCode
          }
        }).then((response) => {
          loading.value = false;
          let data = response.data;
          if (data.success) {
            dailyTrainStations.value = data.content.list;
            // 设置分页控件的值
            pagination.value.current = param.pageNum;
            pagination.value.total = data.content.total;
          } else {
            notification.error({description: data.message});
          }
        });
      };

      const handleTableChange = (page) => {
        // console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
        pagination.value.pageSize = page.pageSize;
        handleQuery({
          pageNum: page.current,
          pageSize: page.pageSize
        });
      };

      onMounted(() => {
        handleQuery({
          pageNum: 1,
          pageSize: pagination.value.pageSize
        });
      });

      return {
        dailyTrainStation,
        visible,
        dailyTrainStations,
        pagination,
        columns,
        handleTableChange,
        handleQuery,
        loading,
        onAdd,
        handleOk,
        onEdit,
        onDelete,
        paramss
      };
    },
  });
</script>
