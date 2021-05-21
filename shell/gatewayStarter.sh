PROJECT_NAME="gateway"
SPRING_PROFILES_ACTIVE="dev"
SPRINGBOOT_JAR=""
SPRINGBOOT_HOME=""
cd "../"
HOME=$(pwd)
SPRINGBOOT_HOME=${HOME}/${PROJECT_NAME}
echo "${SPRINGBOOT_HOME}"
if [ -z "${SPRINGBOOT_JAR}" ]; then
  JARS_IN_HOME=$(ls ${SPRINGBOOT_HOME}/target/*.jar)
  _i=0
  for JAR in $JARS_IN_HOME
  do
    _i=$((_i + 1))
    echo "Deploying ${JAR}...."
    if [ ${_i} -gt 1 ]; then
      echo "Can only have one start jar file in lemon home $SPRINGBOOT_HOME"
      exit 1
    fi
    SPRINGBOOT_JAR=$JAR
  done
fi
echo "init logs dir"
# Log files
LOG_DIR="${HOME}/logs/${PROJECT_NAME}"
`mkdir -p "${LOG_DIR}"`
OUT_FILE="${LOG_DIR}/console.log"
GC_FILE="${LOG_DIR}/gc.log"
DUMP_PATH="${LOG_DIR}/"
echo "init JAVA_OPTS"
JAVA_OPTS="$JAVA_OPTS -server -XX:+Inline -XX:+UseCompressedOops"
JAVA_OPTS="$JAVA_OPTS -Xms512m -Xmx512m -Xmn128m -XX:SurvivorRatio=8 -XX:InitialSurvivorRatio=8"
JAVA_OPTS="$JAVA_OPTS -XX:MaxTenuringThreshold=6  -XX:ParallelGCThreads=2"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+ParallelRefProcEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="$JAVA_OPTS -Xss256k"
JAVA_OPTS="$JAVA_OPTS -verbose:gc -XX:+DisableExplicitGC -XX:-PrintGC -XX:-PrintGCDetails  -Xloggc:${GC_FILE}"
SPRINGBOOT_OPTS=" -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} "

RUN_CMD="java ${JAVA_OPTS} -jar ${SPRINGBOOT_JAR} ${SPRINGBOOT_OPTS}"
echo "${RUN_CMD}"
echo "${RUN_CMD}" >> "${OUT_FILE}"
nohup ${RUN_CMD} >> ${OUT_FILE} 2>&1 &