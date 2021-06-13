<template>
  <div class="main" v-if="!error">
    <iframe :srcdoc="html"></iframe>
  </div>
  <div v-else class="noReport">
    <strong>This folder 📁 has nothing to report yet</strong>
  </div>
</template>

<script>
import {onMounted, ref, watch} from "vue";
import { useRoute } from "vue-router";

export default {
  setup() {
    const html = ref("");
    const route = useRoute();
    const error = ref(false);
    async function loadReport(id) {
      if (!id) {
        return;
      }
      const reportResponse =  await fetch(
          `http://localhost:8080/reports/${route.params.id}`
      );
      html.value = await reportResponse.text();
      error.value = html.value === "HttpStatus.INTERNAL_SERVER_ERROR";
    }
    onMounted(() => loadReport(route.params.id));
    watch(
        () => route.params,
        (newParams) => {
          if (route.name === "Report") {
            loadReport(newParams.id);
          }
        }
    );
    return {
      html,
      error,
    };
  },
};
</script>
<style scoped>
.main {
  height: 100%;
  display: flex;
  flex-direction: column;
}

iframe {
  flex: 1;
}
.noReport{
  text-align: center;
}
</style>