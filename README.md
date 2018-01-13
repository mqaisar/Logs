# RESTFul API for accessing system logs
Micro-service which gets you access of /var/log as a RESTful API. The service is written in SpringBoot and runs under dokcer container.

## How to
1. At the root of the repo you will find docker-compose.yaml. Run the following command to start the setup installed and running
    #### docker-compose up -d ####
2. The definition of the API can be found here: https://app.swaggerhub.com/apis/qach1/Logs/v1
Required Header: 'api-key' with value equals 'Mai9Xzpijfe3o'

### Note:
1. This service will return only those logs under /var/log which are accessible to the current user who starts the application.
If you want to access logs file under sudo then you have to set 'config.SUDO_PASS' to sudo password and 'config.USER_ROLE' to 'admin' in 'resources/application.properties' file.
 
2. How many day's old logs i.e. 7 is not hardcoded but can be configured in 'resources/application.properties' file which is set default to 7.

3. Run tests using 'sudo' otherwise they will fail. Reason is that tests write file into /var/log directory for testing purposes which is protected.

4. 'api-key' is set to 'Mai9Xzpijfe3o' in configuration file. This is not the right way but it is done for proof of concept.    
