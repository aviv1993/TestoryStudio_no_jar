<template>
  <form class="needs-validation" novalidate id="storyForm" @submit="submitForm" >
    <div class="body">
      <nav class="navbar navbar-dark">
        <div class="container-fluid">
          <span class="navbar-brand mdi mdi-alpha-s-box-outline" > {{storyName}}</span>
        </div>
      </nav>
      <Editor
        v-if="!shouldShowBlocklyTab || !isBlockly"
        id="ace"
        :body="getStoryBody()"
        @getContent="getContent"
        @autoSave="autoSave"
        :getContentNow="getContentNow"
        :isReadOnly="isBlockly"
      />
      <Blockly
        v-if="isBlockly"
        id="blockly"
        :options="genBlocklyOptions(eventBlocklyDefs)"
        ref="blockly"
        :xml="storyBody"
        @getContent="getContent"
        :getContentNow="getContentNow"
        @autoSave="autoSave"
        :hidden="!shouldShowBlocklyTab"
      />
    </div>
    <div>
      <button v-if="newStory" type="submit" class="btn btn-primary">Add Story</button>
    </div>
  </form>
</template>

<script>
import Editor from "./Editor";
import { ref, onMounted, watch, computed } from "vue";
import { getStory, addStory, updateStory, BlocklyType } from "../services/storyService";
import { getFolderEventsDefs } from "../services/folderService"
import { useRoute,useRouter } from "vue-router";
import Blockly from "./Blockly"
import genBlocklyOptions from "../js/bp_blocks"

export default {
  components: { Editor, Blockly },
  setup(props, { emit }) {
    const blockly = ref(null) // Ref to blockly component
    const isBlockly = ref(false);
    const type = ref(null);
    const shouldShowBlocklyTab = ref(false);
    const formValidationMessage=ref("");
    const storyName = ref("");
    const getContentNow = ref(false);
    const storyBody = ref("");
    const route = useRoute();
    const router = useRouter();
    const successMessageAdd = ref(false);
    const successMessageUpdate = ref(false);
    const alertMessage = ref(false);
    const newStory = computed(() => !route.params.id);
    const eventBlocklyDefs = ref(null)
    async function loadStory(id) {
      if (!id) {
        eventBlocklyDefs.value = await getFolderEventsDefs(route.query.parentId);
        storyName.value = "";
        storyBody.value = "bthread('', function () {\n\n});";
        return;
      }
      const story = await getStory(id);
      eventBlocklyDefs.value = await getFolderEventsDefs(story.parentId);
      storyName.value = story.name;
      storyBody.value = story.body;
      isBlockly.value = story.type == BlocklyType;
      shouldShowBlocklyTab.value = isBlockly.value;
      type.value = story.type;
    }
    onMounted(() => loadStory(route.params.id));
    watch(
      () => route.params,
      (newParams) => {
        if (route.name === "Story") {
          loadStory(newParams.id);
        }
      }
    );
    function checkNameValidity(){
      formValidationMessage.value='';
      if (!storyName.value || storyName.value === "") {
        formValidationMessage.value="Story name can't be empty"
      }
      else if (storyName.value.includes(" ")) {
        formValidationMessage.value="Story name must not contain any spaces";
      }
    }

    function isValid(form,event){
      checkNameValidity();
      if ( !form.checkValidity()||formValidationMessage.value!='' )
      {
          document.getElementById('storyName').setCustomValidity( formValidationMessage.value);
          event.preventDefault()
          event.stopPropagation()
          return false;
      }
      else
      {
          document.getElementById('storyName').setCustomValidity( '' );
          return true;
      }
    }

    function submitForm(event) {
      const form = document.getElementById("storyForm");
      checkNameValidity();
      if (isValid(form,event))
        getContentNow.value = true;
      form.classList.add('was-validated')
      document.getElementById('storyName').addEventListener('input', function() {isValid(form,event)})

    }

    async function getContent(content) {
      storyBody.value = content;
      getContentNow.value = false;
      if (newStory.value) {
        const story = {
          name: storyName.value,
          body: storyBody.value,
          parentId: route.query.parentId,
          type: type.value,
          translatedBody: isBlockly.value ? blockly.value.getJsCode() : null
        };
        const result = await addStory(story);
        if (result != -1) {
          successMessageAdd.value = true;
          router.push(`/tree/story/${result}`);
        }
        else{
          alertMessage.value = true;
        }
      } else {
        const result = updateStoryHelper(content);
        if(result){
          successMessageUpdate.value = true;
        }
        else{
         alertMessage.value = true; 
        }
      }
      emit("reloadTree");
    }

    function autoSave(content){
      if(newStory.value)
        return;
      const result = updateStoryHelper(content);
      if(!result){
        alertMessage.value = true; 
      }
    }
    
    function updateStoryHelper(bodyToUpdate){
      if (bodyToUpdate === undefined && isBlockly.value)
      {
        bodyToUpdate = blockly.value.getContent()
      }
      const story = {
          name: storyName.value,
          body: bodyToUpdate,
          id: route.params.id,
          type: type.value,
          translatedBody: isBlockly.value ? blockly.value.getJsCode() : null
        };
        return updateStory(story);
    }

    function restartMessages() {
      successMessageAdd.value = false;
      successMessageUpdate.value = false;
      alertMessage.value = false;
      
    }
    
    function getStoryBody() {
      if (isBlockly.value){
        return blockly.value.getJsCode()
      }
      else{
        return storyBody.value
      }
    }
    /*
    const blocklyOptions = {
      media: 'media/',
      grid:
        {
          spacing: 25,
          length: 3,
          colour: '#ccc',
          snap: true
        },
      toolbox: genBpToolBox(eventBlocklyDefs.value)
    }*/

    function toggleStoryType() {
      this.isBlockly = !this.isBlockly;
      this.shouldShowBlocklyTab =this.isBlockly;
      storyBody.value = isBlockly.value ? undefined :"bthread('', function () {\n\n});";
    }

    return {
      storyName,
      storyBody,
      getContent,
      getContentNow,
      submitForm,
      newStory,
      props,
      formValidationMessage,
      checkNameValidity,
      isValid,
      successMessageAdd,
      successMessageUpdate,
      restartMessages,
      alertMessage,
      autoSave,
      updateStoryHelper,
      isBlockly,
      type,
      shouldShowBlocklyTab,
      toggleStoryType,
      getStoryBody,
      blockly,
      eventBlocklyDefs,
      genBlocklyOptions
    };
  },
};
</script>

<style scoped>

.navbar-dark {
  background: black;
}

#blockly {
  flex: 1;
}

span{
  font-size:1.25rem;
  font-weight:bold;
  display: block;
  padding: 0;
}

#ace {
    flex: 1;
}

.body {
    flex: 1;
    display: flex;
    flex-direction: column;
}

form {
    height: 100%;
    display: flex;
    flex-direction: column;
}
</style>
