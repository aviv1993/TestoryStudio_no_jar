import { reactive, watch } from "vue";

export default function useFetch(url, requestOptions) {
    const state = reactive({
        loading: false,
        error: '',
        data: null
    })

    const fetchData = async () => {
        state.loading = true;
        state.data = null;
        state.error = '';
        if (!url.value) {
            state.loading = false;
            return;
        }
        try {
            const response = await fetch(url.value, requestOptions.value);
            if (response.ok) {
                const json = await response.json()
                state.data = json;
            }
            else {
                state.error = 'Error while preforming the request.';
            }
        } catch (error) {
            state.error = `Error! ${error.message}`;
        }

        state.loading = false;
    }

    watch(() => url.value, () => {
        console.log('Watched!');
        fetchData();
    });
    fetchData();
    return state;
}