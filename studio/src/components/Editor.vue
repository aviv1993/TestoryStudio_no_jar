<template>
  <div class="editor-container">
    <div id="editor"></div>
  </div>
</template>

<script>
import ace from "ace-builds";
import { onMounted, toRef, watch } from "vue";
import { useRoute } from 'vue-router'
import { defineEventRegex, bthreadRegex, innerFuncRegex, threeArgsRegex, twoArgsRegex } from "../stores/regexes.js";

export default {
  props: {
    getContentNow: Boolean,
    body: String,
    isReadOnly: Boolean
  },
  setup(props, { emit }) {
    const getContentNow = toRef(props, "getContentNow");
    const body = toRef(props, "body");
    const isReadOnly = toRef(props, "isReadOnly");
    const route = useRoute();
    function setupAce() {
      ace.config.set(
        "basePath",
        "https://cdn.jsdelivr.net/npm/ace-builds@1.4.12/src-noconflict/"
      );
      const editor = ace.edit("editor");
      editor.setTheme("ace/theme/monokai");
      editor.session.setMode("ace/mode/javascript");
      editor.setValue(body.value);
      editor.setReadOnly(isReadOnly.value)
      editor.setOption("showPrintMargin", false)
      editor.getSession().setValue(body.value);
      editor.on("change", function() {
        emit("autoSave", editor.getValue());
        parseContent();
      });
      editor.getSession().on("changeAnnotation", function(){

        const annotations = editor.getSession().getAnnotations();
        for (const annotation in annotations){
          if(annotations[annotation].type === 'error') {
            console.error('Error', annotations[annotation]);
          }
        }

      });
    }
    function getContent() {
      const editor = ace.edit("editor");
      return editor.getValue();
    }

    function parseContent() {
      const editor = ace.edit("editor");
      const content = editor.getValue();
      
      let match;
      let pad = 0;
      while ((match = defineEventRegex.exec(content)) !== null) {
        let eventName=match[3];
        if(eventName.charAt(0)<'A' || eventName.charAt(0)>'Z'){
          displayWarning( match.index + match[2].length+pad, "First argument (event name) must start with a capital letter.");
          return;
        }
        checkInnerFunctions(match[5],match.index + match[1].length+pad+1, true);
        pad=pad+match[0].length;
      }

      while ((match = bthreadRegex.exec(content)) !== null) {
        checkInnerFunctions(match[5], match.index + match[1].length+pad+1, false);
        pad=pad+match[0].length;
      }

      
    }
    function checkInnerFunctions(content, pad, isEvent) {
      let match;
      while ((match = innerFuncRegex.exec(content)) !== null) {
        let funcName = match[2];
        if(funcName.charAt(0)<'a' || funcName.charAt(0)>'z'){
          displayWarning(pad, `In function name "${funcName}" :Function name must start with a lowercase letter.`);
          return;
        }
        if(isEvent&& pad<0 && checkFuncArgs(match[4], pad+match[2].length+1)<0){
          return;
        }
        pad=pad+match[0].length
      }
    }

    function checkFuncArgs(content,pad) {
      let match;
      while ((match = threeArgsRegex.exec(content)) !== null||((match = threeArgsRegex.exec(content)) === null && (match = twoArgsRegex.exec(content)) !== null)) {
        let xpath = match[3];
        if (xpath.length<3 || xpath.charAt(0)!='"' || xpath.charAt(1)!='/' || xpath.charAt(xpath.length-1)!='"'){
          displayWarning(pad+match[1].length+match[2].length+1, "Second argument is not identified as xpath")
          return -1;
        }
        pad = pad+match.length;
      }
      return 1;
    }

    function displayWarning(index, message) {
      const editor = ace.edit("editor");
      var pos = editor.session.doc.indexToPosition(index);
      console.log(pos.row, pos.column);
      console.log(message);
      var annotations=editor.getSession().getAnnotations();
      annotations.push(
        {
          row: pos.row,
          column: pos.column,
          text: message,
          type: "warning" // also "warning" and "information"
        }
      )
      editor.getSession().setAnnotations(annotations);
      //editor.session.setOption("useWorker", false)
    }

    watch(
      () => getContentNow.value,
      current => {
        if (current) {
          emit("getContent", getContent());
        }
      }
    );

    watch(
      () => body.value,
      current => {
        const editor = ace.edit("editor");
        editor.getSession().setValue(current);
        const selectText = route.query.hightlightText;
        if(selectText){
          console.log(selectText)
          editor.findAll(selectText,{
            wrap: true,
            caseSensitive: true, 
            wholeWord: true,
            regExp: false,
            preventScroll: true // do not change selection
          })
        }
        
      }
    );

    onMounted(() => setupAce());
    return {};
  },
  name: "Editor"
};
</script>

<style scoped>
#editor {
    width: 100%;
    height: 100%;
}
.editor-container {
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    display: block;
}
</style>
