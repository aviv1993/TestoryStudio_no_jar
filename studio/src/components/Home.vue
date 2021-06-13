<template>
  <div class="centered">
    <img :src="require(`../assets/testory2.jpg`)" width="600" height="400"  />
  </div>
</template>

<script>
export default {
  setup() {},
};
</script>

<style>
.centered {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}
</style>