CURRENT_DATE_UTC=`date --utc -Iseconds`
while true; do
  sleep 60
  echo "logs since ${CURRENT_DATE_UTC}"
  gcloud logging read "resource.type=gce_instance AND resource.labels.instance_id=8113029603229598688" --format json timestamp>=\"${CURRENT_DATE_UTC}\" ... > logfile.txt
  cat logfile.txt | sed '/^$/d'
  if [[ $(cat logfile.txt | head -n 5 | wc -l) -ne 0 ]]; then
    CURRENT_DATE_UTC=`date --utc -Iseconds`;
  fi
done