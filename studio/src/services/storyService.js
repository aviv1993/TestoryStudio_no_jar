const urlPrefix = "http://localhost:8080/stories";
export const BlocklyType = "BLOCKLY";
export const JSType = "JS";

export async function addStory(story) {
    const url = urlPrefix;
    const requestOptions = {
        method: "post",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(story),
    };
    const response = await  fetch(url, requestOptions);
    return await response.json();
}

export async function getStory(id) {
    const url = urlPrefix + `/${id}`;
    const response = await fetch(url);
    return await response.json();
}

export async function updateStory(story) {
    const url = urlPrefix + `/${story.id}`;
    const requestOptions = {
        method: "put",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(story),
    };
    return await fetch(url, requestOptions);
}

export async function renameStory(id,name) {
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

export async function deleteStory(id) {
    const url = urlPrefix + `/${id}`;
    const requestOptions = {
        method: "delete",
    };
    return await fetch(url, requestOptions);
}