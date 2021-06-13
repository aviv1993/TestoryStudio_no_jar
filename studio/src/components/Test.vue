<template>
    <div class="mb-3">
      <label for="testName" class="form-label" required>Test name</label>
      <input class="form-control" id="testName" v-model="testName" />
      <div class="invalid-feedback">Test name can not be empty!</div>
    </div>
  <table class="table">
    <thead>
      <tr>
        <th>Last Run</th>
        <th>Pased</th>
        <th></th>
      </tr>
    </thead>
    <tr>
      <td>2020-12-13</td>
      <td><i class="mdi mdi-check-circle" /></td>
      <td><a href="#">results</a></td>
    </tr>
    <tr>
      <td>2020-12-10</td>
      <td><i class="mdi mdi-alpha-x-circle" /></td>
      <td><a href="#">results</a></td>
    </tr>
  </table>
  <button
    type="submit"
    class="btn"
    @click="addNewTest"
    :class="isNewTest ? 'btn-primary' : 'btn-warning'"
  >
    {{ isNewTest ? "Add" : "Update" }} Test
  </button>
  &nbsp;
  <button v-if="!isNewTest" class="btn btn-danger" v-on:click= "delTest" >Delete Test</button>
</template>

<script>
import { ref, computed, onMounted, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import  checkedBoxes  from "../stores/checkedBoxesStore"
import { addTest, getTest, deleteTest } from "../services/testService"

export default{
  emits: ["reloadTree"],
  setup(props, { emit }){
    const router = useRouter();
    const route = useRoute();
    const isNewTest = computed(() => !route.params.id)
    const testName = ref("");

    async function loadTest(testId) {
      if (!isNewTest.value){
        const test = await getTest(testId)
        testName.value = test.name;
      }
    }
    onMounted(() => loadTest(route.params.id));
    watch(
      () => route.params,
      (newParams) => {
        if (route.name === "Test") {
          console.log(isNewTest.value)
          loadTest(newParams.id);
        }
      }
    );

    async function delTest() {
      const res = await deleteTest(route.params.id)
      if (res.ok){
        router.push({name: 'Tree', params: {isTestsTree: true}})
      }
      emit("reloadTree")
    }

    async function addNewTest(){
      const foldersToRun = Object.keys(checkedBoxes.value).filter((key) => checkedBoxes.value[key])
      const test = {name: testName.value, parentId: undefined, testFoldersIds: foldersToRun}
      const res = await addTest(test)
      if (res.ok)
      {
        router.push({name: 'Tree', params: {isTestsTree: true}})
      }
    }
    return {
      testName,
      isNewTest,
      addNewTest,
      delTest
    };
  }
}
</script>
