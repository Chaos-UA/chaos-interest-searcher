npm start

sudo docker run -p 9200:9200 -p 9300:9300 -v /media/chaos/Chaos/elastic-data-badoo:/usr/share/elasticsearch/data -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.8.6