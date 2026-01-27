<template>
  <div>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </p>
    <a-table :dataSource="passengers"
             :columns="columns"
             :pagination="pagination"
             @change="handleTableChange"
             :loading="loading">
      <template #bodyCell = "{ column, record}">
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
            <a @click = "onEdit(record)">编辑</a>
          </a-space>
        </template>
      </template>
    </a-table>
    <a-modal v-model:visible="visible" title="乘车人" @ok="handleOk"
             ok-text="确认" cancel-text="取消">
      <a-form :model="passenger" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
        <a-form-item label="姓名">
          <a-input v-model:value="passenger.name" />
        </a-form-item>
        <a-form-item label="身份证">
          <a-input v-model:value="passenger.idCard" />
        </a-form-item>
        <a-form-item label="旅客类型">
          <a-select v-model:value="passenger.type">
            <a-select-option value="1">成人</a-select-option>
            <a-select-option value="2">儿童</a-select-option>
            <a-select-option value="3">学生</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>

import {ref, defineComponent, onMounted} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "passenger-view",
  setup() {
    const visible = ref(false);
    const passenger = ref({
      id: undefined,
      memberId: undefined,
      idCard: undefined,
      type: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const passengers = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 3,
    });
    let loading = ref(false);
    const columns = [
      {
        title: '姓名',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '身份证',
        dataIndex: 'idCard',
        key: 'idCard',
      },
      {
        title: '旅客类型',
        dataIndex: 'type',
        key: 'type',
      },
      {
        title: '操作',
        dataIndex: 'operation',
      },
    ];

    const handleQuery = (params) => {
      if (!params) {
        params = {
          pageNum: 1,
          pageSize: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("member/passenger/query-list", {
        params: {
          pageNum: params.pageNum,
          pageSize: params.pageSize
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          passengers.value = data.content.list;
          // 设置分页控件的值
          pagination.value.current = params.pageNum;
          pagination.value.total = data.content.total;
        } else {
          notification.error({description: data.message})
        }
      });
    }

    const onAdd = () => {
      visible.value = true;
    };

    const onEdit = (record) => {
      passenger.value = record;
      visible.value =true;
    };

    const handleOk = () => {
      axios.post("member/passenger/save", passenger.value).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.info({description: "保存乘车人成功"});
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

    const handleTableChange = (pagination) => {
      handleQuery({
        pageNum: pagination.value.current,
        pageSize: pagination.value.pageSize,
      })
    };

    onMounted(() => {
      handleQuery({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      })
    });

    return {
      passenger,
      passengers,
      pagination,
      handleTableChange,
      handleQuery,
      columns,
      visible,
      onAdd,
      onEdit,
      handleOk,
      loading
    };
  }
})
</script>

<style></style>