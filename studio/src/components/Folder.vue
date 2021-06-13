<template>
  <form id="folderForm" @submit.prevent="submitForm">
      <div
      v-if="successMessageAdd||successMessageUpdate "
      class="alert alert-success alert-dismissible fade show"
      role="alert"
    >
      <strong>{{successMessageAdd? "Folder successfully added!": "Folder successfully updated!" }}</strong> 
      <button
        type="button"
        class="btn-close"
        data-bs-dismiss="alert"
        aria-label="Close"
        v-on:click= "restartMessages"
      ></button>
    </div>
    <div
      v-if="alertMessage"
      class="alert alert-danger alert-dismissible fade show"
      role="alert"
    ><strong>Ooops! Something went wront. Try again later</strong>
      <button
        type="button"
        class="btn-close"
        data-bs-dismiss="alert"
        aria-label="Close"
        v-on:click= "restartMessages"
      ></button>
    </div>
    <div class="mb-3">
      <label for="folderName" class="form-label" required>Folder name</label>
      <input class="form-control" id="folderName" v-model="folderName" />
      <div class="invalid-feedback">Folder name can not be empty!</div>
    </div>
    <button
      type="submit"
      class="btn"
      :class="newFolder ? 'btn-primary' : 'btn-warning'"
    >
      {{ newFolder ? "Add" : "Update" }} Folder
    </button>
    &nbsp;
  </form>
</template>

<script>
import { ref, onMounted, watch, computed } from "vue";
import { getFolder, addFolder, updateFolder } from "../services/folderService";
import { useRoute,useRouter } from "vue-router";
export default {
  setup(props, { emit }) {
    const folderName = ref("");
    const route = useRoute();
    const router = useRouter();
    const successMessageAdd = ref(false);
    const successMessageUpdate = ref(false);
    const alertMessage = ref(false);


    const newFolder = computed(() => !route.params.id);

    async function loadFolder(id) {
      if (!id) {
        folderName.value = "";
        return;
      }
      const folder = await getFolder(id);
      folderName.value = folder.name;
    }
    onMounted(() => loadFolder(route.params.id));

    watch(
      () => route.params,
      (newParams) => {
        if (route.name === "Folder") {
          loadFolder(newParams.id);
        }
      }
    );

    async function submitForm() {
      if(newFolder.value) {
        const folder={
          name: folderName.value,
          parentId: route.query.parentId,
        }
        const result=await addFolder(folder);
        if(result!=-1){
          successMessageAdd.value = true;
          router.push(`/tree/folder/${result}`);
        }
        else{
          alertMessage.value = true;
        }
      }
      else{
        const folder={
        id:route.params.id,
        name: folderName.value,
        }
        const result=await updateFolder(folder);
        if(result){
          successMessageUpdate.value = true;
        }
        else{
          alertMessage.value = true;
        }
      }
        emit("reloadTree");
    }

    function restartMessages() {
      successMessageAdd.value = false;
      successMessageUpdate.value = false;
      alertMessage.value = false;
      
    }


    return {
      folderName,
      submitForm,
      newFolder,
      props,
      successMessageAdd,
      successMessageUpdate,
      restartMessages,
      alertMessage,
    };
  },
};
</script>

<style>
</style>
