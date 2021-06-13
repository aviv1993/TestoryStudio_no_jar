<template>
  <div class="split">
    <div id="split-0">
      <ul class="list-group tree">
        <div v-if="!treeData" class="centered">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>
        <div v-if="treeData">
          <div
            v-if="isTestsTree"
            style="margin-top: 10px; margin-bottom: 10px;"
          >
            <router-link
              :to="{ name: 'AddTest', params: { isAdditionTree: true } }"
            >
              <button class="btn btn-primary">Create New Test</button>
            </router-link>
          </div>
          <div v-if="!isTestAdditionTree && !isTestsTree && !isReportsTree">
              <i id= "addMainFolder" class="mdi mdi-folder-plus" type="button" @click="() => togglePopup()" />
          </div>
          <TreeItem
            v-for="item in treeData"
            :item="item"
            :key="item.id"
            @reloadTree="reloadTree"
          ></TreeItem>
        </div>
      </ul>
    </div>
    <div id="split-1">
      <router-view @reloadTree="reloadTree" />
    </div>
  </div>
  <Popup 
			v-if="popupButtonTrigger"
			:togglePopup="() => togglePopup()"
      :id= null
      :fileType="'folder'"
      :action="'Add'"
      @reloadTree= "reloadTree"
      @toggle= '"true"'>
		</Popup>
</template>

<script>
import TreeItem from "./TreeItem";
import Popup from "./Popup";


import getTree from "../services/treeService";
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import Split from "split.js";
import { getCurrentSizes, setCurrentSizes } from "../stores/splitterStore"

export default {
  setup() {
    const treeData = ref(null);
    const route = useRoute();
    const isTestsTree = ref(route.params.type == "tests");
    const isReportsTree = ref(route.params.type == "reports");
    const isTestAdditionTree = ref(route.params.isAdditionTree);
    const type = ref(route.params.type);
    const popupButtonTrigger = ref(false);
    const togglePopup = () => {
      popupButtonTrigger.value = !popupButtonTrigger.value
		}

    onMounted(() => {
      Split(["#split-0", "#split-1"], {
        sizes: getCurrentSizes(),
        onDragEnd: function (sizes) {
          setCurrentSizes(sizes);
        },
      });

      reloadTree();
    });

    async function reloadTree() {
      console.log("Reload Tree");
      const tree = await getTree(isTestsTree.value);
      treeData.value = tree;
    }

    return {
      treeData,
      TreeItem,
      reloadTree,
      isTestsTree,
      isTestAdditionTree,
      Popup,
			popupButtonTrigger,
			togglePopup,
      isReportsTree,type
    };
  },
  name: "Tree"
};
</script>

<style>
.tree {
}
.centered {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.split {
  display: flex;
  flex-direction: row;
  height: calc(100vh - 57px);
}

#split-0{
    overflow: auto;
}

#split-1 {
  display: block;
  overflow: auto;
}

.gutter {
  background-color: #eee;
  background-repeat: no-repeat;
  background-position: 50%;
}

.gutter.gutter-horizontal {
  background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAeCAYAAADkftS9AAAAIklEQVQoU2M4c+bMfxAGAgYYmwGrIIiDjrELjpo5aiZeMwF+yNnOs5KSvgAAAABJRU5ErkJggg==");
  cursor: col-resize;
}
</style>
