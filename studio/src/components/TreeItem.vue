<template>
  <li class="list-group-item" v-if="!isReportsTree || isFolder">
    <div style="display: flex; justify-content: space-between" @contextmenu="openMenu($event)">
      <div v-if="isTestAdditionItem">
        <div @click="toggle">
          <h5>
            <i class="mdi" :class="logo" />
            {{ item.name }}
          </h5>
        </div>
      </div>
      <div v-else-if="isReportsTree">
        <div @click="getReport">
          <h5 :class="{selected: selected == id}" type="button">
            <i class="mdi" :class="logo" type="button" />
            {{ item.name }}
          </h5>
        </div>
      </div>
      <div v-else>
        <router-link
          :to="{ name: routeTo, params: { id: id, type:type} }"
        >
          <div @click="toggle">
            <h5 :class="{selected: selected == id}">
              <i class="mdi" :class="logo" />
              {{ item.name }}
            </h5>
          </div>
        </router-link>
      </div>
    <ul class="right-click-menu" tabindex="-1" v-if="viewMenu&&!isTestAdditionItem&&!isReportsTree" v-bind:style="{ top: contextMenuTop + 'px', left: contextMenuLeft + 'px'}" @mouseleave="closeMenu">
       <li v-if="isFolder&&item.name!='Events'" id="addStory" @click="() => togglePopup('story', 'Add')" type="button" class="mdi mdi-alpha-s-box-outline"> Add Story </li>
       <li v-if="isFolder&&item.name!='Stories'" id="addEvent" @click="() => togglePopup('eventDef', 'Add')" type="button" class="mdi mdi-alpha-e-box-outline"> Add Event </li>
       <li v-if="isFolder" @click="() => {togglePopup('folder', 'Add')}" type="button" class="mdi mdi-folder-plus"> Add Folder </li>
       <li id='rename' @click="() => togglePopup(fileType, 'Rename')" type="button" class=" mdi mdi-lead-pencil"> Rename </li>
       <router-link :to="{ name: 'RunTest', params: { id: id } }"> <li v-if="isFolder" id="runTest" class="mdi mdi-arrow-right-drop-circle-outline"> Run Test </li></router-link>
       <li @click="del" id="deleteButton" type="button" class="mdi mdi-delete"> Delete </li>
     </ul>
      <div v-if="isFolder && !isTestsTree">
        <div v-if="isTestAdditionItem">
          <input type="checkbox" v-model="isChecked" />
        </div>
      </div>
      <div v-else-if="isTest">
        <router-link :to="{ name: 'RunTest', params: { id: id } }">
          <i class="mdi mdi-arrow-right-drop-circle-outline" />
        </router-link>
        <router-link :to="{ name: 'Folder', query: { parentId: id } }">
          <i class="mdi mdi-folder-plus" />
        </router-link>
      </div>
    </div>

    <ul class="list-group list-group-flush" v-show="isOpen" v-if="isFolder || isTest">
      <TreeItem
        v-for="child in item.children"
        :key="child.id"
        :item="child"
      ></TreeItem>
    </ul>
  </li>
    <Popup 
			v-if="popupButtonTrigger"
			:togglePopup="() => togglePopup('','Close')"
      :id= "id"
      :fileType= "fileTypetoPass"
      :action= "action"
      @reloadTree= "reload"
      @toggle= "toggle">
		</Popup>
</template>

<script>
import { deleteFolder } from "../services/folderService";
import { deleteStory } from "../services/storyService";
import { deleteEvent } from "../services/eventService";
import {ref, computed, toRef, watch, nextTick} from "vue";
import { useRoute, useRouter } from "vue-router";
import checkedBoxes from "../stores/checkedBoxesStore";
import { setIsOpen, getIsOpen, setSelected, getSelected } from "../stores/itemStore"
import Popup from "./Popup";

const fileToLogoMap = {
  eventDef: "mdi-alpha-e-box-outline",
  story: "mdi-alpha-s-box-outline",
  folderOpen: "mdi-folder-open",
  folder: "mdi-folder",
  test: "mdi-alpha-t-box-outline"
};

const fileToRouteComponentMap = {
  eventDef: "Event",
  story: "Story",
  folderOpen: "Tree",
  folder: "Tree",
  test: "Test"
};
export default {
  props: {
    item: Object
  },
  emits: ["reloadTree"],
  setup(props, { emit }) {
    const item = toRef(props, "item");
    const contextMenuTop = ref("0");
    const contextMenuLeft = ref("0");
    const router = useRouter();
    const routeTo = computed(() => fileToRouteComponentMap[item.value.file]);
    const route = useRoute();
    const isReportsTree = ref(route.params.type == "reports");
    const isOpen = ref(getIsOpen(item.value.id));
    const selected = getSelected()
    const isFolder = computed(() => item.value.file === "folder");
    const fileType=computed(() => item.value.file);
    const isChecked = ref(false);
    const isTest = computed(() => item.value.file === "test");
    const id = item.value.id;
    const viewMenu = ref(false);
    const action=ref("Add")
    const fileTypetoPass=ref("")
    const isTestAdditionItem = ref(route.params.isAdditionTree);
    const popupButtonTrigger = ref(false);
    const isTestsTree = computed(() => route.params.type == "tests");
    const type = ref(route.params.type);

		const togglePopup = (ft, actionToTake) => {
      fileTypetoPass.value=ft;
      action.value = actionToTake; 
      popupButtonTrigger.value = !popupButtonTrigger.value
      emit("reloadTree");
		}

    function reload() {
      emit('reloadTree');
    }
    
    function toggle() {
      if (isFolder.value || isTest.value) {
        isOpen.value = !isOpen.value;
        setIsOpen(item.value.id, isOpen.value);
      }
      setSelected(item.value.id)
    }

    function getReport() {
      if(!isOpen.value){
        let hasFolders = false;
        item.value.children.forEach(element => { 
          if(element.file === "folder"){
            hasFolders = true;
          }  
        });
        if(hasFolders){
          isOpen.value = true;
          setIsOpen(item.value.id, isOpen.value);
          setSelected(item.value.id);
          router.push({name: 'Report', params: {id:id, type:"reports"}})
        }
        else{
          setSelected(item.value.id);
          router.push({name: 'Report', params: {id:id, type:"reports"}})
        }
      }
      else{
        isOpen.value = false;
        setIsOpen(item.value.id, isOpen.value);
        router.push({name: 'Tree', params: {id:id, type:"reports"}})
        setSelected(false);
      }
    }

    watch(isChecked, () => {
      checkedBoxes.value[id] = isChecked.value;
    });

    const logo = computed(() => {
      if (isFolder.value && isOpen.value) {
        return fileToLogoMap["folderOpen"];
      } else {
        return fileToLogoMap[item.value.file];
      }
    });

    async function openMenu(e){
      viewMenu.value = true;
      await nextTick();
      const contextMenuMargin = 2; // Should be the same as margin under 'right-click-menu' style.
      const contextMenuWidth = 200;
      const contextMenus = Array.from(document.getElementsByClassName("right-click-menu"));
      let contextMenuHeight;
      if(isFolder.value){
        // find the highest
        contextMenuHeight = Math.max(contextMenus.map(cm => cm.offsetHeight))
      } else {
        contextMenuHeight = Math.min(contextMenus.map(cm => cm.offsetHeight))
      }
      if (e.pageX + contextMenuWidth > window.innerWidth + window.screenX) {
        contextMenuLeft.value = e.pageX - contextMenuWidth - contextMenuMargin;
      } else {
        contextMenuLeft.value = e.pageX - contextMenuMargin;
      }

      if (e.pageY + contextMenuHeight > window.innerHeight + window.scrollY) {
        contextMenuTop.value = e.pageY - contextMenuHeight - contextMenuMargin;
      } else {
        contextMenuTop.value = e.pageY - contextMenuMargin;
      }

      e.preventDefault();
     }
    function closeMenu() {
      viewMenu.value = false;
    }

    window.addEventListener("click", function() {
      viewMenu.value = false;
    });

    async function del() {
      if (fileType.value === "story") {
        await deleteStory(id);
      } 
      else if (fileType.value === "eventDef"){
        await deleteEvent(id);
      }
      else if(fileType.value === "folder"){
        await deleteFolder(id);
      }
      router.push('/tree')
      emit("reloadTree");

    }

    return {
      contextMenuTop,
      contextMenuLeft,
      isOpen,
      toggle,
      isFolder,
      id,
      logo,
      routeTo,
      openMenu,
      closeMenu,
      viewMenu,
      del,
      isTestAdditionItem,
      isChecked,
      isTest,
      isTestsTree,
      selected,
      Popup,
			popupButtonTrigger,
			togglePopup,
      fileTypetoPass,
      fileType,
      action,
      reload,
      isReportsTree,
      getReport,
      type
    };
  },
  name: "TreeItem"
};
</script>

<style>
li {
  overflow: hidden;
  display: flex;

}
li a {
  color: black;
  text-decoration: none;
}

.right-click-menu{
    background: #FAFAFA;
    border: 1px solid #BDBDBD;
    box-shadow: 0 2px 2px 0 rgba(0,0,0,.14),0 3px 1px -2px rgba(0,0,0,.2),0 1px 5px 0 rgba(0,0,0,.12);
    display: block;
    list-style: none;
    padding: 0;
    position: fixed;
    width: 200px;
    z-index: 999999;
    margin: 2px 2px 2px 2px;
}

.right-click-menu li {
  border-bottom: 1px solid #e0e0e0;
  margin: 0;
  padding: 5px 35px;
}

.right-click-menu li:last-child {
  border-bottom: none;
}

.right-click-menu li:hover {
  background: #1e88e5;
  color: #fafafa;
}

.selected {
  color: blue;
}
</style>
