//import testsTree from "../mocks/testsTreeMock"
export default async function getTree(isTestsTree) {
  if(isTestsTree){
      const response = await fetch("http://localhost:8080/tests");
      return response.json();
    }
    const url = `http://localhost:8080/tree`;
    const response = await fetch(url);
    return response.json();
}
