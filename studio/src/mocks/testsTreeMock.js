import { ref } from 'vue'

const testsTree = ref([
    {
        "id": 28,
        "name": "tests",
        "file": "folder",
        "children": [
            {
                "id": 33,
                "name": "test1",
                "file": "test",
                "children": []
            },
            {
                "id": 29,
                "name": "test2",
                "file": "test",
                "children": []
            },
        ]
    },
    {
        "id": 30,
        "name": "test",
        "file": "test",
        "children": []
    }
]);

export default testsTree;
