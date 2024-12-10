# Ateliers de prise en main de Docker
## Atelier 1. télécharger et installer docker sur une VM Ubuntu 22.04.1
1. Aller sur le site : [docker-desktop](https://www.docker.com/products/docker-desktop)
2. Télécharger la version compatible avec votre machine. <br/><b>Remarques : </b><i>Docker Desktop exige une version récente d'OS, sinon il faut passer par un toolbox-docker-desktop.</i>
3. Mettre à jour la liste des packages existants sur votre machine : <br/>```sudo apt update```
4. Installer les paquets permettant à APT d'utiliser des paquets via HTTPS : <br/>```sudo apt install apt-transport-https ca-certificates curl software-properties-common```
5. Ajouter la clé GPG du référentiel officiel Docker à votre système : <br/>```curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -```
6. Ajouter le référentiel Docker aux sources APT : <br/>```sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"``` <br/><b>Remarques : </b><i>Cela mettra également à jour votre base de données de packages avec les packages Docker du référentiel nouvellement ajouté.</i>
7. Vérifier le cache APT pour vous assurer que vous êtes sur le point d'installer à partir du référentiel Docker au lieu du référentiel Ubuntu par défaut : <br/>```apt-cache policy docker-ce``` <br/><b>Remarques : </b><i>Noter que docker-ce n'est pas encore installé, mais le candidat à l'installation provient du référentiel Docker pour Ubuntu 22.04</i>
8. Installer Docker : <br/>```sudo apt install docker-ce```
9. Docker est maintenant installé et le démon est démarré. Vérifier qu'il est en cours d'exécution :<br/>```sudo systemctl status docker``` <br/><b>Remarques : </b><i>L'output de la commande doit montrer que le service est actif et en cours d’exécution.</i>
10. Si vous souhaitez éviter de taper 'sudo' à chaque fois que vous exécutez la commande docker, ajoutez votre nom d'utilisateur (username) au groupe docker. <br/>```sudo usermod -aG docker username```
11. Pour appliquer la nouvelle appartenance au groupe, déconnectez-vous du serveur et reconnectez-vous, ou tout simplement saisissez la commande suivante : <br/>```su - username``` <br/><b>Remarques : </b><i>Vous serez invité à saisir votre mot de passe utilisateur pour continuer.</i>
12. Confirmer que votre utilisateur est maintenant ajouté au groupe Docker en tapant la commande : <br/>```groups```
13. Vérifier que Docker fonctionne en affichant sa version par exemple : <br/>```docker version```

## Atelier 2. manipuler les images et les conteneurs Docker
1. Utiliser Docker consiste à lui passer une chaîne d'options et de sous-commandes suivies d'arguments. La syntaxe prend la forme suivante :<br/>```docker [option] [subcommand] [arguments]```<br/>Pour afficher toutes les sous-commandes Docker disponibles, taper :<br/>```docker```<br/>Vérifier le résultat et analyser certaines sous-commandes. Pour afficher les options disponibles pour une sous-commande spécifique, saisissez :<br/>```docker subcommand --help```<br/>E.g. ```docker build --help```
2. Pour vérifier si vous pouvez accéder aux images de Docker Hub et les télécharger, saisissez :<br/>```docker run hello-world```<br/>L'output indiquera que Docker fonctionne correctement et qu'il n'a initialement pas pu trouver l'image hello-world localement, il a donc téléchargé l'image depuis Docker Hub, qui est le référentiel par défaut.
3. Vous pouvez rechercher des images disponibles sur Docker Hub en utilisant la commande docker avec la sous-commande search. Par exemple, pour rechercher l'image Ubuntu, saisissez :<br/>```docker search ubuntu```<br/>Cette commande explorera Docker Hub et renverra une liste de toutes les images dont le nom correspond à la chaîne de recherche.<br/><b>Remarques : </b><i>La colonne [OK] indique une image construite et soutenue par l'organisation derrière le projet.</i>
4. Une fois que vous ayez identifié l’image que vous souhaitez utiliser, vous pouvez la télécharger sur votre machine locale à l’aide de la sous-commande pull.<br/>E.g. exécuter la commande suivante pour télécharger l'image officielle d'Ubuntu sur votre machine locale :<br/>```docker pull ubuntu```
5. Pour voir les images qui ont été téléchargées sur votre machine, taper :<br/>```docker images```
6. Exécuter des conteneur Docker :<br/>Le conteneur hello-world que vous avez exécuté à l'étape précédente est un exemple de conteneur qui s'exécute et se ferme après avoir émis un message de test. Les conteneurs peuvent être bien plus utiles que cela et ils peuvent être interactifs. Exécuter un conteneur en utilisant la dernière image d'Ubuntu : <br/>```docker run -it ubuntu```<br/><b>Remarques : </b><i>La combinaison des options -i et -t vous donne un accès shell interactif au conteneur.</i>
7. Après avoir utilisé Docker pendant un certain temps, vous aurez de nombreux conteneurs actifs (en cours d'exécution) et inactifs sur votre ordinateur. Pour afficher ceux qui sont actifs, utilisez :<br/>```docker ps```
8. Pour afficher tous les conteneurs, actifs et inactifs, exécuter 'docker ps' avec l'option -a :<br/>```docker ps -a```
9. Pour afficher le dernier conteneur que vous avez créé, exécuter 'docker ps' avec l'option -l :<br/>```docker ps -l```
10. Pour démarrer un conteneur arrêté, utiliser 'docker start', suivi de l’ID du conteneur ou de son nom :<br/>E.g. ```docker start 1c08a7a0d0e4```
11. Pour arrêter un conteneur en cours d'exécution, utiliser 'docker stop', suivi de l'ID ou du nom du conteneur : <br/>E.g. ```docker stop my_running_container```
12. Pour supprimer un conteneur, utiliser 'docker rm', suivi de l'ID ou du nom du conteneur : <br/>E.g. ```docker rm my_container```

## Atelier 3. créer un Dockerfile, générer l'Image Docker et exécuter un conteneur Docker
### 1. Image say_hello_one
1. Dans votre répertore de travail, créer un sous-répertoire say_hello_one : <br/>```mkdir say_hello_one```
2. placez-vous dans le répertoire say_hello_one et créer un fichier vierge appelé Dockerfile (sans extension)
3. compléter le fichier avec les commandes Dockerfile suivantes :<br/>
  ```
	#créer une image à partir d'une image de base officielle alpine version 3.14 enregistrée dans Docker Hub
	FROM alpine:3.14
	#exécuter la commande linux : echo "Hello One"
	CMD ["echo", "Hello One"]
  ```
4. Lister vos images : <br/>```docker image ls```
5. Lister vos conteneurs : <br/>```docker container  ls```
6. Générer l'image say_hello_one : <br/>```docker build --tag say_hello_one .```
7. Lister encore une fois vos images : <br/>```docker image ls```<br/>Vous venez de créer votre première Image Docker, félicitations !<br/>Remarquez la date de création de l'image ainsi que sa taille.
8. Exécuter le conteneur say_hello_one : <br/>```docker run say_hello_one```<br/>Le message 'Hello One' est affiché sur votre console. Félicitations, vous venez d'exécuter votre premier conteneur Docker. 
### 2. Image say_hello_two
1. Dans votre répertore de travail, créer un sous-répertoire say_hello_two : <br/>```mkdir say_hello_two```
2. placez-vous dans le répertoire say_hello_two et créer un fichier vierge appelé Dockerfile (sans extension)
3. compléter le fichier avec les commandes Dockerfile suivantes :
   ```
	#créer une image à partir d'une image de base officielle ubuntu version 20.04 enregistrée dans Docker Hub
	FROM ubuntu:20.04
	#exécuter la commande linux : echo "Hello Two"
	CMD ["echo", "Hello Two"]
   ```
4. Lister vos images : <br/>```docker image ls```
5. Lister vos conteneurs : <br/>```docker container  ls```
6. Générer l'image say_hello_two : <br/>```docker build --tag say_hello_two .```
7. Lister encore une fois vos images : <br/>```docker image ls```<br/>Comparer la date de création et la taille des images say_hello_one et say_hello_two
8. Exécuter le conteneur say_hello_two en mode intéractif : <br/>```docker run --interactive --tty say_hello_two /bin/bash```
### 3. Image say_hello_three
1. Dans votre répertore de travail, créer un sous-répertoire say_hello_three : <br/>```mkdir say_hello_three```
2. Placez-vous  dans le répertoire say_hello_three et créer un fichier source d'une application node js simple [say_hello_three.js](https://github.com/Cloud-Elit/Docker_Kubernetes/blob/main/say_hello_three.js)<br/>
   ```
	const http = require('http');
	const server = http.createServer((req, res) => {
	  res.writeHead(200, { 'Content-Type': 'text/plain' });
	  res.end('Hello, je suis un script JS!\n');
	});
	const port = process.env.PORT || 8080;
	server.listen(port, () => {
	  console.log(`Server running on http://localhost:${port}`);
	});
   ```
3. Dans le répertoire say_hello_three créer un fichier vierge appelé Dockerfile (sans extension)
4. compléter le fichier avec les commandes Dockerfile suivantes :<br/>
```
#créer une image à partir d'une image de base officielle Node;js version 14 enregistrée dans Docker Hub
FROM node:14
#dans le conteneur, le répertoire de travail sera '/usr/src/app'
WORKDIR /usr/src/app
#copier tpus les packages json ainsi que mon application (say_hello_three.js) dans le conteneur
COPY package*.json say_hello_three.js ./
#installer les dépendances de l'application
RUN npm install
#copier tous les autres fichiers de l'application (images, etc.) dans le conteneur
COPY . .
#exposer le port sur lequel l'application sera accessible
EXPOSE 8080
#enfin, la commande pour lancer l'application
CMD ["node", "say_hello_three.js"]
```
5. Générer l'image say_hello_two : <br/>```docker build --tag say_hello_three .```
6. Exécuter le conteneur say_hello_three en mode intéractif : <br/>```docker run -p 8080:8080 say_hello_three```<br/>Votre application est maintenant accessible via ```http://localhost:8080```
				
## Atelier 4. publier une image sur le repository Docker Hub
1. Si vous n'avez pas de compte et de dépôt (repository) public sur Docker Hub, vous devez d'abord les créer :
- Aller sur : https://hub.docker.com/
- Créer un compte et garder le nom d'utilisateur qui vous sera utile par la suite (E.g. mohamedtalha)
- Créer un dépot (repository) public (E.g. test_images)
2. Pour diffuser (push) votre image, vous devez vous connecter à Docker Hub via la commande : <br/>```docker login -u docker-registry-username```E.g. ```docker login -u mohamedtalha```<br/>Vous serez invité à vous authentifier à l’aide de votre mot de passe Docker Hub.
3. Si votre nom d'utilisateur Docker Hub est différent du nom d'utilisateur local que vous avez utilisé pour créer l'image, vous devrez baliser (tag) votre image avec votre nom d'utilisateur Docker Hub : <br/>```docker tag image:version docker-registry-username/repository:version```E.g. ```docker tag say_hello_three:latest mohamedtalha/test_images:lastest```
4. Vous pouvez désormais diffuser votre propre image via la commande : <br/>```docker push docker-registry-username/docker-image-name```E.g. ```docker push mohamedtalha/test_images:lastest```
5. Vérifier votre image dans votre référentiel public. Après avoir envoyé une image vers un registre, elle devrait être répertoriée sur le tableau de bord (dashboard) de votre compte : https://hub.docker.com/

