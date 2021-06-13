const testsApi = "http://localhost:8080/tests"

export async function connectToWebSocket() {
    const url = "http://localhost:8080/ws";
    const response = await fetch(url);
    console.log(response)
    return response.text();
}

export async function runTest(folderId, socketId) {
    const url = new URL("http://localhost:8080/commands/runTest")
    const params = {
        folderId: folderId,
        socketId: socketId
    }
    url.search = new URLSearchParams(params).toString();

    return await fetch(url, {
        method: 'post'
    })
}

export async function addTest(test) {
  const requestOptions = {
      method: "post",
      headers: {
          "Content-Type": "application/json"
      },
      body: JSON.stringify(test),
  };
  return await fetch(testsApi, requestOptions);
}


export async function getTest(id) {
  const url = testsApi + `/${id}`;
  const response = await fetch(url);
  return await response.json()
}

export async function deleteTest(id) {
  const url = testsApi + `/${id}`;
  const requestOptions = {
      method: "delete",
  };
  return await fetch(url, requestOptions);
}
export async function getReportUrl(folderId) {
   return `http://localhost:8081/#/tree/report/${folderId}`
}

export async function getReport(folderId) {
    const reportResponse =  await fetch(
        `http://localhost:8080/reports/${folderId}`
    );
    return await reportResponse.text();
}

