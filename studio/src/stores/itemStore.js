export function getIsOpen(itemId) {
  // var strItems = isReportTree? 'itemsReport' : 'items';
  var items = localStorage.getItem('items');
  if(items) {
    return JSON.parse(items)[itemId] || false;
  } else {
    return false;
  }
}

export function setIsOpen(itemId, isOpen) {
  // var strItems = isReportTree? 'itemsReport' : 'items';
  var items = localStorage.getItem('items');
  if(items) {
    const currentState = JSON.parse(items);
    currentState[itemId] = isOpen;
    localStorage.setItem('items', JSON.stringify(currentState));
  } else {
    localStorage.setItem('items', JSON.stringify({itemId: isOpen}));
  }
}

import { ref } from 'vue'

const selectedItem = ref(-1)

export function getSelected() {
  var item = localStorage.getItem('selected-item')

  if(item) {
    selectedItem.value = parseInt(item)
    return selectedItem
  } else {
    return selectedItem
  }
}

export function setSelected(itemId) {
  localStorage.setItem('selected-item', itemId.toString())
  selectedItem.value = itemId
}