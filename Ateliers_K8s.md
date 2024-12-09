# Ateliers de prise en main de Kubernetes
#### Environnement de travail :
	- Windows 8.1	
	- JDK 17.0.12	
	- Maven 3.6.3
	- VirtualBox 6.1
	- IntelliJ IDEA 2021.3.3 (Community Edition)

## Atelier 1. Installer minikube & kubectl
1. Télécharger minikube-installer.exe du site officiel [Minikube](https://minikube.sigs.k8s.io/docs/start/?arch=%2Fwindows%2Fx86-64%2Fstable%2F.exe+download)
2. Exécuter minikube-installer.exe et suivre les étapes
3. Ouvrir une console PowerShell (ou une invite de commande) et exécuter la commande suivante : <br/>```minikube start```<br/><b>Remarques : </b><i>Si la commande minikube n'est pas reconnu, trouver le répertoire d'installation de minikube sur votre machine, et ajouter le dans votre PATH</i>
4. Vérifiez que minikube est bien installé en exécutant la commande suivante : <br/>```minikube status```<br/>Cette commande doit vous lister les status du host, kubelet, apiserver (RUNNING) et kubeconfig (CONFIGURED)
5. Installer kubectl en exécutant la commande suivante : <br/>```minikube kubectl -- get pods```<br/><b>Remarques : </b><i>Si kubectl est déjà installé, vous aurez le message "No resources found in default namespace"</i>
	
## Atelier 2. Créer une API REST avec Spring Boot
1. Créer un projet Spring Boot vide
    1. Aller sur [Spring Initializr](https://start.spring.io/)
    2. Compléter le formulaire :
       - Project : Maven
       - Language : Java
       - Spring Boot : 3.4.0
       - Group : training.mtalha
       - Artifact : rest-api-spring-boot-k8s
       - Name : rest-api-spring-boot-k8s
       - Description : POC REST API Spring Boot and K8s
       - Package name : training.mtalha.rest-api-spring-boot-k8s
       - Packaging : jar
       - Java : 17
    3. Cliquer sur "ADD DEPENDENCIES" et choisir les dépendances suivantes :
       - Spring Web
       - Lombok
     <br/><br/>Voici une capture d'écran du formulaire Spring Initializr :<br/>
       ![Spring_Initializr](https://github.com/user-attachments/assets/6011e732-c6ef-4ec9-a676-aa56e84c0e5a)
    4. Cliquer sur le bouton GENERATE, un fichier rest-api-spring-boot-k8s.zip sera téléchargé sur votre répertoire de téléchargement par défaut. Dézippez-le.
    5. Ouvrez IntelliJ IDEA, et aller dans File > Open... et naviguer jusqu'au dossier dézippé à l'étape précédente
    6. Appuyer sur Open, faites confiance au projet, et ouvrez le nouveau projet dans la même fenêtre IntelliJ.
    7. Une fois votre projet ouvert et indexé, créer une API REST en suivant les étapes suivantes :
       - bouton-droit sur le package "training.mtalha.rest_api_spring_boot_k8s" et créer un sous-package "rest.controller"
       - bouton-droit sur le package "rest.controller" et créer la classe MyRestApi
       - Saisir le code suivant pour la classe MyRestApi :
         ```java
	        package training.mtalha.rest_api_spring_boot_k8s.rest.controller;
	
		import lombok.Getter;
		import lombok.Setter;
		import org.springframework.http.HttpStatus;
		import org.springframework.http.ResponseEntity;
		import org.springframework.web.bind.annotation.GetMapping;
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RestController;
		
		/**
		 * @author Mohamed, TALHA
		 * @created 07-12-2024
		 *
		 * This is Rest API just returns an object which has id, name, place, and value.
		 *
		 */
		@RestController
		@RequestMapping("/home")
		public class MyRestApi {
		
		    @GetMapping("/info")
		    public ResponseEntity<ResponseData> getInfo() {
		        ResponseData responseData = new ResponseData();
		        responseData.setId(1);
		        responseData.setName("ENSA");
		        responseData.setPlace("Marrakech");
		        return new ResponseEntity<ResponseData>(responseData, HttpStatus.OK);
		    }
		
		    @Getter
		    @Setter
		    public class ResponseData {
		        private String name;
		        private Integer id;
		        private String place;
		    }
		}
         ```
       - Générer le package du projet : <br/>```mvn clean package -DskipTests=true```<br/><b>Remarques : </b><i>vérifier que le package <b>rest-api-spring-boot-k8s-0.0.1-SNAPSHOT.jar</b> est bien généré dans le répertoire : rest-api-spring-boot-k8s\target\</i>			
	
## Atelier 3. Créer le Dockerfile et générer l'Image Docker
	3.01. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créer un fichier vide appelé Dockerfile
	3.02. Compléter le Dockerfile avec les commandes suivantes :
	#-----------------------------------------#
	FROM openjdk:17-alpine
	COPY ./target/rest-api-spring-boot-k8s-0.0.1-SNAPSHOT.jar app.jar
	ENTRYPOINT exec java -jar app.jar --debug
	#-----------------------------------------#
	3.03. ouvrez une console PowerShell (ou l'invite de commande) lancer minikube :
		>> minikube start
	3.04. vérifier que le cluster K8s est bien démarré :
		>> minikube status
		R.Q. vous devriez obtenir le résultat suivant :
		type: Control Plane
		host: Running
		kubelet: Running
		apiserver: Running
		kubeconfig: Configured
	3.05. accéder au noeud du cluster K8s en ssh :
		>> minikube ssh
		un terminal ssh de minikube est alors ouvert pour pouvoir exécuter des commandes linux
	3.06. vérifier que Docker est bien installé :
		>> docker version
	3.07. lister les images existantes :
		>> docker image ls
	3.08. générer l'image grâce à l'utiliser "build" de Docker
		3.08.a. positionnez-vous dans votre répertoire de travail du projet Java :
		E.g. cd /c/Users/Mohamed/Downloads/rest-api-spring-boot-k8s
		3.08.b. exécuter la commande build pour générer l'image Docker :
		>>  docker build --file=Dockerfile --tag=rest-api-spring-boot-k8s:1.0.0 .
		3.08.c. Vérifiez l'image :
		>> docker image ls
		Vous devriez avoir un résultat comme celui-là :
		REPOSITORY                  TAG        IMAGE ID       CREATED          SIZE
		rest-api-spring-boot-k8s    1.0.0      3975d3e8c3a5   57 seconds ago   125MB
	3.09. Quitter le terminal ssh :
		>> exit
		
## Atelier 4. Créer les objets K8s requis pour le déploiement et le lancement de l'application
	4.01. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créer un fichier vide appelé deployment.yaml
	4.02. Compléter le deployment.yaml comme suit :
	----------------------------------------------------------------------------------	
	apiVersion: apps/v1
	kind: Deployment
	metadata:
	  name: rest-api-spring-boot-k8s
	spec:
	  selector:
		  matchLabels:
			app: rest-api-spring-boot-k8s

	  replicas: 1
	  template:
		metadata:
		  labels:
			app: rest-api-spring-boot-k8s
		spec:
		  containers:
			- name: rest-api-spring-boot-k8s
			  image: rest-api-spring-boot-k8s:1.0.0
			  ports:
				- containerPort: 8080
			  env:
				- name: env.namespace
				  value: default
		  volumes:
			- name: application-config
			  configMap:
				name: rest-api-spring-boot-k8s	
	----------------------------------------------------------------------------------
	4.03. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créer un fichier vide appelé service.yaml
	4.04. Compléter le service.yaml comme suit :
	----------------------------------------------------------------------------------
	apiVersion: extensions/v1beta1
	kind: Service
	spec:
	  type: NodePort
	----------------------------------------------------------------------------------

## Atelier 5. Déployer et lancer l'API REST sur minikube
	5.01. Sur la console PowerShell (ou l'invite de commande) positionnez-vous dans votre répertoire de travail du projet Java :
	E.g. cd C:\Users\Mohamed\Downloads\rest-api-spring-boot-k8s
	5.02. créer et explorer un pod sur K8s
		5.02.a. un pod doit être créée via un Deployement le plus souvent (ou au autre workload selon le besoin)
		>> minikube kubectl -- apply -f deployment.yaml
		R.Q. vous devriez avoir un message confirmant la création du déploiment
		deployment.apps/rest-api-spring-boot-k8s created
		5.02.b Vérifier que le déployment est bien créé :		
		>> minikube kubectl -- get deployments
		Cette commande doit vous listé des informations sur le Deployment :
		NAME                       READY   UP-TO-DATE   AVAILABLE   AGE
		rest-api-spring-boot-k8s   0/1     1            0           4m13s
		5.02.c. Vérifier qu'un pod à bien été lancé par le déploiement :
		>> minikube kubectl -- get pods
		Cette commande doit vous listé au moins un pod :
		NAME                                        READY   STATUS    RESTARTS     AGE
		rest-api-spring-boot-k8s-7899bf44b6-c6fj4   1/1     Running   1 (3s ago)   7s
		5.02.d. Afficher les informations du pod :
		>> minikube kubectl -- describe pod rest-api-spring-boot-k8s-7899bf44b6-c6fj4
		5.02.e. Afficher les log du pod :
		>> minikube kubectl -- logs rest-api-spring-boot-k8s-7899bf44b6-c6fj4
		R.Q. analyser les logs et vérifier qu'il n'y a pas d'erreur dans l'application. En cas d'erreur, il faudra corriger l'application, regénérer le jar, re-build le Dockerfile pour avoir une nouvelle image, etc. 		
	5.03. exposer votre service :
	>> minikube kubectl -- apply -f service.yaml
	R.Q. on peut aussi exposer le service comme suit : minikube kubectl expose deployment rest-api-spring-boot-k8s --type=NodePort
	R.Q. vous devriez avoir un message confirmant la création du service
	Vous pouvez vérifier que le service est bien créé :
	>> minikube kubectl -- get services
	5.04. A ce stade, l'application est déployée, le service est exposé, vous pouvez récupérer l'URL du service grâce à la commande suivante :
	>> minikube service rest-api-spring-boot-k8s-service --url
	R.Q. Cette commande vous renvoit l'URL du service :
	E.g. http://192.168.59.100:31728
	5.05. Dans un navigateur web, appeler l'endpoint de votre API : http://192.168.59.100:31728/home/info
