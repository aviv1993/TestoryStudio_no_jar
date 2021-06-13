# TestoryStudio
## Run locally
1. Install postgresql
    1. set username to ``postgres`` (default value)
    2. set the password to ``1111`` (Or you can change to password inside ``spring.datasource.password`` in the file src/main/resources/application-dev.properties)
2. Install pgAdmin
3. Create new database named TestoryDatabase (Or change the default name of ``spring.datasource.url`` in the same file as the password one)
4. Clone the repo
5. run ``mvn install``
5. run
    ``` bash
    cd studio
    npm install
    npm run serve
    ```
6. Run the main function of TestoryStudioApplication class
7. Open ``http://localhost:8081/``

## Running with remote SQL Server
1. Make sure to have the following enviroment variables defined
    1. TESTORY_DATASOURCE_USERNAME
    2. TESTORY_DATASOURCE_URL
    3. TESTORY_DATASOURCE_PASSWORD
    4. If you just defined them make sure to restart you IDE
2. Update ``src/main/resources/application.properties`` to ``spring.profiles.active=prod``
