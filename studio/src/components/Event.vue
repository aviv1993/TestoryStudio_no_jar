<template>
  <form class="needs-validation" novalidate id="eventForm" @submit="submitForm">
    <nav class="navbar navbar-dark">
      <div class="container-fluid">
        <span class="navbar-brand mdi mdi-alpha-e-box-outline" > {{eventName}}</span>
      </div>
    </nav>
    <div class="body">
      <Editor
        id="ace"
        :body="eventBody"
        @getContent="getContent"
        @autoSave="autoSave"
        :getContentNow="getContentNow"
        :isReadOnly="false"
      />
    </div>
  <div>
    <button v-if="newEvent" type="submit" class="btn btn-primary"> Add Event </button>
  </div>
  </form>

  
</template>

<script>
import Editor from "./Editor";
import { ref, onMounted, watch, computed } from "vue";
import { getEvent, addEvent, updateEvent, } from "../services/eventService";
import { useRoute,useRouter } from "vue-router";
export default {
  components: { Editor },
  setup(props, { emit }) {
    const formValidationMessage=ref("");
    const eventName = ref("");
    const getContentNow = ref(false);
    const eventBody = ref("");
    const route = useRoute();
    const router = useRouter();
    const successMessageAdd = ref(false);
    const successMessageUpdate = ref(false);
    const alertMessage = ref(false);

    const newEvent = computed(() => !route.params.id);
    

    async function loadEvent(id) {
      if (!id) {
        eventName.value = "";
        eventBody.value = "define_event('', function (e){\n\n});";
        return;
      }
      const event = await getEvent(id);
      eventName.value = event.name;
      eventBody.value = event.body;
    }
    onMounted(() => loadEvent(route.params.id));

    watch(
      () => route.params,
      (newParams) => {
        if (route.name === "Event") {
          loadEvent(newParams.id);
        }
      }
    );
    
    function checkNameValidity(){
      formValidationMessage.value='';
      if (!eventName.value || eventName.value === "") {
        formValidationMessage.value="Event name can't be empty"
      }
      else if (eventName.value.charAt(0)<'A'||eventName.value.charAt(0)>'Z') {
        formValidationMessage.value="Event name must begin with a capital letter"
      }
      else if (eventName.value.includes(" ")) {
        formValidationMessage.value="Event name must not contain any spaces"
      }


    }

    function isValid(form,event){
      checkNameValidity();
      if ( !form.checkValidity()||formValidationMessage.value!='' )
      {
          document.getElementById('eventName').setCustomValidity( formValidationMessage.value);
          event.preventDefault()
          event.stopPropagation()
          return false;
      }
      else
      {
          document.getElementById('eventName').setCustomValidity( '' );
          return true;
      }
    }

    function submitForm(event) {
      const form = document.getElementById("eventForm");
      checkNameValidity();
      if (isValid(form,event))
        getContentNow.value = true;
      form.classList.add('was-validated')
      document.getElementById('eventName').addEventListener('input', function() {isValid(form,event)})

    }

    async function getContent(content) {
      eventBody.value = content;
      getContentNow.value = false;
      if (newEvent.value) {
        const event = {
          name: eventName.value,
          body: eventBody.value,
          parentId: route.query.parentId,
        };
        const result = await addEvent(event);
        if(result!=-1){
          successMessageAdd.value = true;
          router.push(`/tree/event/${result}`);
        }
        else{
          alertMessage.value = true;
        }

      } else {
        const result = updateEventHelper(content);
        if(result){
          successMessageUpdate.value = true;
        }
        else{
          alertMessage.value = true;
        }

      }
      emit("reloadTree");
    }

    function autoSave(bodyToUpdate){
      if(newEvent.value) 
        return;
      const result = updateEventHelper(bodyToUpdate);
      if(!result){
        alertMessage.value = true;
      }
    }

    function updateEventHelper(content){
      const event = {
          name: eventName.value,
          body: content,
          id: route.params.id,
        };
        return updateEvent(event);
    }
    function restartMessages() {
      successMessageAdd.value = false;
      successMessageUpdate.value = false;
      alertMessage.value = false;
      
    }

    return {
      eventName,
      eventBody,
      getContent,
      getContentNow,
      submitForm,
      newEvent,
      props,
      formValidationMessage,
      checkNameValidity,
      isValid,
      successMessageAdd,
      successMessageUpdate,
      restartMessages,
      alertMessage,
      autoSave,
      updateEventHelper,
    };
  },
};
</script>

<style scoped>

span{
  font-size:1.25rem;
  font-weight:bold;
  line-height:1rem;
  display: block;
}

.navbar-dark {
  background: black;
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
