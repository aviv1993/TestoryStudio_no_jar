const urlPrefix = "http://localhost:8080/folders";

export async function addFolder(folder) {
    const url = urlPrefix;
    const requestOptions = {
        method: "post",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(folder),
    };
    const response = await  fetch(url, requestOptions);
    return await response.json();
}

export async function getFolder(id) {
    const url = urlPrefix + `/${id}`;
    const response = await fetch(url);
    return await response.json();
}

export async function updateFolder(folder) {
    const url = urlPrefix + `/${folder.id}`;
    const requestOptions = {
        method: "put",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(folder),
    };
    return await fetch(url, requestOptions);
}

export async function renameFolder(id,name) {
    const url = urlPrefix + `/rename_${id}`;
    const requestOptions = {
        method: "put",
        headers: {
            "Content-Type": "application/json"
        },
        body: name,
    };
    return await fetch(url, requestOptions);
}

export async function deleteFolder(id) {
    const url = urlPrefix + `/${id}`;
    const requestOptions = {
        method: "delete",
    };
    return await fetch(url, requestOptions);
}

export async function getFolderEventsDefs(id){
    const url = urlPrefix + `/defs/${id}`;
    const response = await fetch(url);
    return await response.json();
}