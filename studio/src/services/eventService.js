const urlPrefix = "http://localhost:8080/events";

export async function addEvent(event) {
    const url = urlPrefix;
    const requestOptions = {
        method: "post",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(event),
    };
    const response = await  fetch(url, requestOptions);
    return await response.json();
}

export async function getEvent(id) {
    const url = urlPrefix + `/${id}`;
    const response = await fetch(url);
    return await response.json();
}

export async function deleteEvent(id) {
    const url = urlPrefix + `/${id}`;
    const requestOptions = {
        method: "delete",
    };
    return await fetch(url, requestOptions);
}

export async function updateEvent(event) {
    const url = urlPrefix + `/${event.id}`;
    const requestOptions = {
        method: "put",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(event),
    };
    return await fetch(url, requestOptions);
}

export async function renameEvent(id,name) {
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