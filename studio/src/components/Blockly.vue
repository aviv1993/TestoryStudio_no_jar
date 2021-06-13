<template>
  <div>
    <div class="blocklyDiv" ref="blocklyDiv">
    </div>
    <xml ref="blocklyToolbox" style="display:none">
      <slot></slot>
    </xml>
  </div>
</template>

<script>
import Blockly from 'blockly';
import { ref, onMounted, watch, toRef } from "vue";
export default {
  name: 'Blockly',
  props: {options: Object, getContentNow: Boolean, xml: String},
  setup(props, { emit }){
    const workspace = ref(null);
    const blocklyDiv = ref(null);
    const getContentNow = toRef(props, "getContentNow");
    const xml = toRef(props, "xml");

    function getContent() {
      var xml = Blockly.Xml.workspaceToDom(workspace.value);
      return Blockly.Xml.domToText(xml);
    }

    function getJsCode() {
      return Blockly.JavaScript.workspaceToCode(Blockly.mainWorkspace)
    }

    watch(
      () => getContentNow.value,
      current => {
        if (current) {
          emit("getContent", getContent());
        }
      }
    );

    function changeListener(event){
      if (event.type == Blockly.Events.BLOCK_CREATE || event.type == Blockly.Events.BLOCK_CHANGE || event.type == Blockly.Events.BLOCK_MOVE){
        emit("autoSave", undefined)
      }
    }

    function loadXml(){
      if (xml.value != undefined && xml.value != "") {
        const dom_xml = Blockly.Xml.textToDom(xml.value);
        Blockly.mainWorkspace.clear();
        Blockly.Xml.domToWorkspace(dom_xml, Blockly.mainWorkspace);        
      }
    }

    onMounted(() => {
      var options = props.options || {};
      if (!options.toolbox) {
        options.toolbox = this.$refs["blocklyToolbox"];
      }
      options.trashcan = false;
      workspace.value = Blockly.inject(blocklyDiv.value, options);
      // disable scroll bars
      workspace.value.scrollbar.hScroll.svgGroup_.style.display = 'none';
      workspace.value.scrollbar.vScroll.svgGroup_.style.display = 'none';
      workspace.value.addChangeListener(changeListener);
      loadXml()
    });

    watch(
      () => xml.value,
      () => {
        loadXml()
      }
    );
    return { workspace, blocklyDiv, getJsCode, getContent }
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.blocklyDiv {
  height: 100%;
  width: 100%;
  text-align: left;
}
</style>