<template>
  <div class="main">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">
          <img :src="require(`./assets/topLogo1.png`)" height="25"/>
          Testory Studio
        </a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNavAltMarkup"
          aria-controls="navbarNavAltMarkup"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
          <div class="navbar-nav">
            <router-link class="nav-link" :class="{active: routeName === 'Tree' || routeName === 'Story' || routeName === 'Event' || routeName === 'RunTest'}" aria-current="page" :to="{name: 'Tree', params: {type:'editor'}}"
              >Editor</router-link
            >
            <router-link class="nav-link" aria-current="page" :to="{ name: 'Tree', params: {type:'tests'}}"
              >Tests</router-link
            >
            <router-link class="nav-link" :class="{active: routeName === 'Report'}" aria-current="page" :to="{ name: 'Tree', params: {type:'reports'}}"
            >Reports</router-link>
          </div>
        </div>
      </div>
    </nav>
    <router-view :key="$route.params.type"></router-view>
  </div>
</template>

<script>
import "@/styles/mdi.css";
import {onMounted, ref, watch} from "vue";
import {useRoute} from "vue-router";

export default {
  setup() {
    onMounted(() => {
      if (document.getElementById("mdi-icons")) return; // was already loaded
      var linkTag = document.createElement("link");
      linkTag.href =
        "https://cdn.jsdelivr.net/npm/@mdi/font@5.9.55/css/materialdesignicons.min.css";
      linkTag.id = "mdi-icons";
      linkTag.rel = "stylesheet";
      document.getElementsByTagName("head")[0].appendChild(linkTag);
    });
    const route = useRoute();
    const routeName = ref(route.name);
    watch(
        () => route.name,
        (newName) => {
          routeName.value = newName;
        }
    );
    return {
      routeName
    };
  },
  name: "App",
};
</script>

<style>
</style>
