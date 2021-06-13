<template>
  <div class="main">
    <div class="text-center">
      <div v-if='!done'>
        <div>
          <strong>Test Is Running...</strong>
          <div class="spinner-border ms-2" role="status"></div>
        </div>
      </div>
      <div v-if='done' class="alert" :class="{'alert-danger' : !testPassed, 'alert-success': testPassed}">
        <h3 v-if="testPassed">Test Passed</h3>
        <h3 v-else >Test Failed</h3>
      </div>
      <div v-if='done' class="testResults">
        <router-link
            :to="{ name: 'Report', params: { id: folderId, type:'reports' } }"
        >
          <button type="submit" id= "viewReport" class="btn btn-primary">View The Report</button>
        </router-link>
        <button class="btn btn-primary" id= "resetRun" @click="resetRun()">Run again</button>
      </div>
    </div>

    <div class="col-md-2">
      <select id="filter" class="" @change='changeMessagesFilter'>
        <option selected disabled value="">Filter messages by...</option>
        <option>all</option>
        <option>-</option>
        <option>--</option>
        <option>---</option>
      </select>
    </div>
    <div id="scroll" class="body">
      <table id="conversation" class="table">
        <thead>
          <tr>
            <th class="status-col">
              Status
            </th>
            <th class="timestamp-col">
              Timestamp
            </th>
            <th class="details-col">
              Details
            </th>
          </tr>
        </thead>
        <tbody>
        <tr v-for="item in FilteredMessages" :key="item" class="event-row">
          <td><span class="badge" :class="{'bg-info text-dark' : item.severity === 'Info', 'bg-danger' : item.severity === 'Error'}">{{item.severity}}</span></td>
          <td>{{new Date(Date.parse(item.timestamp)).toLocaleString()}}</td>
          <td>{{ item.message }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import { useRoute } from "vue-router";
import { connectToWebSocket, runTest } from "../services/testService";
import { onMounted, ref } from "vue";
export default {
  setup() {
    const route = useRoute();
    const allMessage = ref([]);
    const FilteredMessages = ref([]);
    const done = ref(false);
    const sortBy = ref("all");
    const folderId = ref(route.params.id);
    const testPassed = ref(true);
    function connect(socketId) {
      const socket = new SockJS("http://localhost:8080/wsEndpoint");
      const stompClient = Stomp.over(socket);
      stompClient.connect(
        {},
        () => {
          stompClient.subscribe(`/broker/${socketId}`, (message) => {
            const jsonMessage = JSON.parse(message.body);
            const messageBody = jsonMessage.message;
            allMessage.value.push(jsonMessage);
            if(sortBy.value ==='all'){
              FilteredMessages.value.push(jsonMessage);
            }
            if(sortBy.value ==='-' && messageBody.substring(0, 4) === '  -:'){
              FilteredMessages.value.push(jsonMessage);
            }
            if(sortBy.value ==='--' && messageBody.substring(0, 4) === ' --:'){
               FilteredMessages.value.push(jsonMessage);
            }
            if(sortBy.value ==='---' && messageBody.substring(0, 4) === '---:'){
               FilteredMessages.value.push(jsonMessage);
            }
            const scrollContainer = document.getElementById('scroll');
            scrollContainer.scrollTop = scrollContainer.scrollHeight - scrollContainer.clientHeight;
          });
        },

        () => {
          this.connected = false;
        }
      );
    }

    function changeMessagesFilter(e){
      let temp = allMessage.value;
      sortBy.value = e.target.value;
      if(e.target.value==='all'){
        FilteredMessages.value = allMessage.value;
      }
      if(e.target.value==='-'){
        FilteredMessages.value = temp.filter(x=> x.message.substring(0, 4) === '  -:');
      }
      if(e.target.value==='--'){
        FilteredMessages.value = temp.filter(x=> x.message.substring(0, 4) === ' --:');
      }
      if(e.target.value==='---'){
        FilteredMessages.value = temp.filter(x=> x.message.substring(0, 4) === '---:');
      }
    }

    async function runTestAsync(folderId, socketId) {
      await runTest(folderId, socketId);
    }
    async function resetRun() {
      folderId.value = route.params.id;
      done.value = false;
      allMessage.value = []
      FilteredMessages.value = []
      socketId.value = await connectToWebSocket();
      console.log(socketId.value);
      connect(socketId.value);
      await runTestAsync(route.params.id, socketId.value);
      testPassed.value = !allMessage.value.some(message => message.severity === 'Error' &&
          // for some reason those show up as errors, not better way to tell if test passed or failed
          !message.message.includes("INFO: Detected dialect: W3C") &&
          !message.message.includes("org.openqa.selenium.remote.ProtocolHandshake createSession"));
      done.value = true;

    }
    const socketId = ref("");
    onMounted(resetRun());
    return {
      FilteredMessages,
      done,
      changeMessagesFilter,
      resetRun,
      folderId,
      testPassed
    };
  },
};
</script>


<style scoped>
.body {
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: auto;

}
.table{
  flex: 1;
}

.main {
  height: 100%;
  display: flex;
  flex-direction: column;
}

#conversation {
}

.testResults{
  margin-top: 1rem;
}

.testResults button {
  margin-left: 1rem;
}
</style>