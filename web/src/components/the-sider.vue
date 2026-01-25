<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys2"
        v-model:openKeys="openKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined/>&nbsp;欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined/>&nbsp;乘车人管理
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import {defineComponent, ref} from "vue";

export default defineComponent({
  name: "the-sider-view",
  setup() {
    return {
      selectedKeys2: ref(['1']),
      openKeys: ref(['sub1'])
    };
  },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>