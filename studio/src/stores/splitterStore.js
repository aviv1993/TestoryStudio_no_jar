export function getCurrentSizes() {
  var sizes = localStorage.getItem('split-sizes')

  if (sizes) {
      return JSON.parse(sizes)
  } else {
      return [30, 70] // default sizes
  }
}

export function setCurrentSizes(sizes) {
  localStorage.setItem('split-sizes', JSON.stringify(sizes))
}