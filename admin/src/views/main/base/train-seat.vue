<template>
  <p>
    <a-space>
      <train-selected-view v-model="paramss.trainCode" width="200px"></train-selected-view>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainSeats"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
      </template>
      <template v-else-if="column.dataIndex === 'col'">
        <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.col && item.type === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>

</template>

<script>
  import { defineComponent, ref, onMounted } from 'vue';
  import {notification} from "ant-design-vue";
  import axios from "axios";
  import TrainSelectedView from "@/components/train-selected.vue";

  export default defineComponent({
    name: "train-seat-view",
    components: {TrainSelectedView},
    setup() {
      const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
      const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
      const visible = ref(false);
      let trainSeat = ref({
        id: undefined,
        trainCode: undefined,
        carriageIndex: undefined,
        row: undefined,
        col: undefined,
        seatType: undefined,
        carriageSeatIndex: undefined,
        createTime: undefined,
        updateTime: undefined,
      });
      const trainSeats = ref([]);
      // 分页的三个属性名是固定的
      const pagination = ref({
        total: 0,
        current: 1,
        pageSize: 12,
      });
      let loading = ref(false);
      let paramss = ref({
        trainCode: null
      });
      const columns = [
        {
          title: '车次编号',
          dataIndex: 'trainCode',
          key: 'trainCode',
        },
        {
          title: '厢序',
          dataIndex: 'carriageIndex',
          key: 'carriageIndex',
        },
        {
          title: '排号',
          dataIndex: 'row',
          key: 'row',
        },
        {
          title: '列号',
          dataIndex: 'col',
          key: 'col',
        },
        {
          title: '座位类型',
          dataIndex: 'seatType',
          key: 'seatType',
        },
        {
          title: '同车厢座序',
          dataIndex: 'carriageSeatIndex',
          key: 'carriageSeatIndex',
        },
        {
          title: '操作',
          dataIndex: 'operation'
        }
      ];

      const handleQuery = (param) => {
        if (!param) {
          param = {
            pageNum: 1,
            pageSize: pagination.value.pageSize,
          };
        }
        loading.value = true;
        axios.get("/business/admin/train-seat/query-list", {
          params: {
            pageNum: param.pageNum,
            pageSize: param.pageSize,
            trainCode: paramss.value.trainCode
          }
        }).then((response) => {
          loading.value = false;
          let data = response.data;
          if (data.success) {
            trainSeats.value = data.content.list;
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
        SEAT_COL_ARRAY,
        SEAT_TYPE_ARRAY,
        trainSeat,
        visible,
        trainSeats,
        pagination,
        columns,
        handleTableChange,
        handleQuery,
        loading,
        paramss
      };
    },
  });
</script>
