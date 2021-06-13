<template>
    <div class="popup">
        <div class="popup-inner">
            <form id= "form1">
            <slot />
            <label for="fileName" class="form-label">Enter {{formalFileType()}} Name </label>
            <input class="form-control" v-model= "fileName" type="text" id="fileName" required />
            <div v-if= "fileType=='story' && action=='Add'" id="radioDiv">
                <label class="radio">
                <span class="radio__input">
                    <input type="radio" id="JScript" name="storyType" value= "JScript" checked/>
                    <span class="radio__control"></span>
                </span>
                <span class="radio__label">JScript</span>
                </label>

                <label class="radio">
                <span class="radio__input">
                    <input type="radio" id="Blockly" name="storyType" value= "Blockly" />
                    <span class="radio__control"></span>
                </span>
                <span class="radio__label">Blockly</span>
                </label>

                <label class="radio">
                <span class="radio__input">
                    <input type="radio" id="Rete" name="storyType" value= "Rete" />
                    <span class="radio__control"></span>
                </span>
                <span class="radio__label">Rete</span>
                </label>
            </div>
            <button type= "button" id= "addButton" class="btn btn-primary" 
                @click= "() => {(action=='Add') ? addFile() : rename(); togglePopup()}" >{{action}}
            </button>
            <button type= "button" id= "cancelButton" class="btn btn-danger" @click= "togglePopup()">
                Cancel
            </button>
            </form>
        </div>
    </div>
</template>

<script>
import { addFolder, renameFolder } from "../services/folderService";
import { addStory, renameStory, JSType, BlocklyType } from "../services/storyService";
import { addEvent, renameEvent } from "../services/eventService";
import { setSelected } from "../stores/itemStore"
import { ref, toRef } from "vue";
import { useRouter } from "vue-router";
export default {
    props: ['togglePopup', 'fileType', 'id', 'action'],
    emits: ['reloadTree'],
    setup(props, {emit}){
        const fileType = toRef(props, 'fileType')
        const id = toRef(props, 'id')
        const action = toRef(props, 'action')
        const fileName = ref("")
        const router = useRouter();
        action;

      function formalFileType() {
        if (fileType.value == "story") {
            return 'Story';
        } 
        else if (fileType.value == "eventDef"){
            return 'Event';
        }
        else if(fileType.value == "folder"){
            return 'Folder';
        }
      }

      function toStoryType(popupStoryType) {
        if (popupStoryType == "JScript")
            return JSType;
        else if (popupStoryType == "Blockly")
            return BlocklyType;
      }

        async function addFile() {
            if (fileType.value == "story"){
                createStory(fileName.value);
            }
            else if (fileType.value == "eventDef"){
                createEvent(fileName.value);
            }
            else if (fileType.value == "folder"){
                createFolder(fileName.value);
            }
            emit('reloadTree');


        }

        async function rename() {
            const name=fileName.value;
            if (fileType.value == "story"){
                const result = await renameStory(id.value, name);                
                if(result!=-1){
                    emit('reloadTree');
                    router.push(`/tree`);
                    router.push(`/tree/story/${id.value}`);
                }
            }
            else if (fileType.value == "eventDef"){
                const result = await renameEvent(id.value, name);
                if(result!=-1){
                    emit('reloadTree');
                    router.push(`/tree`);
                    router.push(`/tree/event/${id.value}`);
                }
            }
            else if (fileType.value == "folder"){
                const result = await renameFolder(id.value, name);
                if(result!=-1){
                    emit('reloadTree');
                    router.push(`/tree`);
                    fileName.value=""
                }
            }
            emit('reloadTree');
            setSelected(id.value)

        }
        

        async function createStory(storyName) {
            const storyType=toStoryType(document.querySelector('input[name="storyType"]:checked').value);
            const story = {
                name: storyName,
                body: (storyType === JSType) ? "bthread('', function () {\n\n});" : "",
                parentId: id.value,
                type: storyType,
            };
            const result = await addStory(story);
            if (result != -1) {
                router.push(`/tree/story/${result}`);
                emit('reloadTree');
                setSelected(result);
            }
        }

        async function createEvent(eventName) {
        const event = {
            name: eventName,
            body: "define_event('', function (e){\n\n});",
            parentId: id.value,
            };
        const result = await addEvent(event);
        if(result!=-1){
            router.push(`/tree/event/${result}`);
            emit('reloadTree');
            setSelected(result);
            }
        }

        async function createFolder(folderName) {
        const folder={
            name: folderName,
            parentId: id.value,
            }
        const result=await addFolder(folder);
        if(result!=-1){
            fileName.value="";
            emit('reloadTree');
            setSelected(result);
            }
        }
        return {
            addFile,
            fileName,
            createStory,
            createEvent,
            createFolder,
            rename,
            formalFileType,
            toStoryType
        };
    }
}


</script>

<style scoped>
.popup {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 99;
    background-color: rgba(0, 0, 0, 0.2);
    
    display: flex;
    align-items: center;
    justify-content: center;
    .popup-inner {
        background: #FFF;
        padding: 2rem;
    }
}

input {
    width: 100%;
    padding: 0.75rem 1.25rem;
    margin: 0.1875rem 0;
    box-sizing: border-box;
    
}

button{
    padding: 0.3125rem 2.125rem;
    margin: 0.125rem 0rem;
}

#cancelButton{
    float: right;
}

#form1{
    background-color: Snow;
    padding: 1.5625rem 1.5625rem;
    border-radius: 0.25rem;

}

label{
    color: rgba;
    font-size: 1.0625rem;
}

.radio{
    padding: 0.3125rem 0.875rem;

}

</style>