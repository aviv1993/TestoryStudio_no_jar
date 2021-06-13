import { createRouter, createWebHashHistory } from 'vue-router'
import Editor from '../components/Editor'
import Tree from '../components/Tree'
import Folder from '../components/Folder'
import Event from '../components/Event'
import Story from '../components/Story'
import RunTest from '../components/RunTest'
import Report from '../components/Report'
import Home from '../components/Home'
import Test from "../components/Test"

const routes = [
    { path: '/', component: Home },
    { path: '/editor', component: Editor },
    {
        path: '/tree',
        name: "Tree",
        component: Tree,
        children: [
            {
                path: 'story/:id?',
                component: Story,
                name: "Story"
            },
            {
                path: 'folder/:id?',
                component: Folder,
                name: "Folder"
            },
            {
                path: 'event/:id?',
                component: Event,
                name: "Event"
            },
            {
                path: 'runTest/:id?',
                component: RunTest,
                name: "RunTest"
            },
            {
              path: 'test/',
              component: Test,
              name: "AddTest"
            },
            {
              path: 'test/:id?',
              component: Test,
              name: "Test"
            },
            {
                path: 'reports/:id?',
                component: Report,
                name: "Report"
            }

        ]
    },


]

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

export default router;
