# kart-log-analyser

Analisador de logs de corrida de Kart

# Docker

Crie a imagem executando a tarefa gradle `dockerBuildImage`<br/>

#### Para testar a imagem docker gerada:
Utilizando o Docker diretamente:<br/>
Execute: `docker run -p 8080:8080 --rm "$group/${project.name}:$version"`<br/>
Ou utilize o docker-compose:<br/>
Execute `docker-compose up`

Acesse: http://localhost:8080/swagger-ui.html

Lembre de parar e limpar os containers se usar o compose: `docker-compose down`
