# Ateliers de prise en main de Kubernetes
#### Environnement de travail :
	- Windows 8.1	
	- JDK 17.0.12	
	- Maven 3.6.3
	- VirtualBox 6.1
	- IntelliJ IDEA 2021.3.3 (Community Edition)

## Atelier 1. Installer minikube & kubectl
1. Téléchargez <b>minikube-installer.exe</b> du site officiel [Minikube](https://minikube.sigs.k8s.io/docs/start/?arch=%2Fwindows%2Fx86-64%2Fstable%2F.exe+download) qui s'adapte automatiquement à votre OS.
2. Exécutez minikube-installer.exe et suivez les étapes d'installation.
3. Ouvrez une console PowerShell (ou une invite de commande) et exécutez la commande suivante : <br/>```minikube start```<br/><b>Remarque : </b><i>Si la commande minikube n'est pas reconnu, trouvez le répertoire d'installation de minikube sur votre machine, et ajoutez-le dans votre variable d'environnement PATH</i>
4. Vérifiez que minikube s'est bien installé en exécutant la commande suivante : <br/>```minikube status```<br/>Cette commande doit vous lister les status du host, kubelet, apiserver (RUNNING) et kubeconfig (CONFIGURED) comme suit :
![Capture](https://github.com/user-attachments/assets/66ae0c0d-f9ba-4faf-a12e-fc1b93c6c36b)
6. Installez <b>kubectl</b> en exécutant la commande suivante : <br/>```minikube kubectl -- get pods```<br/><b>Remarque : </b>Si kubectl est déjà installé, vous aurez le message <i>"No resources found in default namespace"</i> sinon il sera téléchargé et installé avant que le message soit affiché.

## Atelier 2. Créer une API REST avec Spring Boot
1. Créez un projet Spring Boot vide
    1. Allez sur [Spring Initializr](https://start.spring.io/)
    2. Complétez le formulaire :
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
    3. Cliquez sur "ADD DEPENDENCIES" et choisissez les dépendances nécessaires au projet, en l'occurence :
       - Spring Web
       - Lombok
     <br/><br/>Voici une capture d'écran du formulaire Spring Initializr :<br/>
       ![Spring_Initializr](https://github.com/user-attachments/assets/6011e732-c6ef-4ec9-a676-aa56e84c0e5a)
    4. Cliquez sur le bouton <b>GENERATE</b>, un fichier rest-api-spring-boot-k8s.zip sera téléchargé sur votre répertoire de téléchargement par défaut. Dézippez-le.
    5. Ouvrez IntelliJ IDEA, et allez dans File > Open... et naviguez jusqu'au dossier dézippé à l'étape précédente
    6. Appuyez sur Open, faites confiance au projet, et ouvrez le nouveau projet dans la même fenêtre IntelliJ.
    7. Une fois votre projet est ouvert et indexé, créez une API REST en suivant les étapes suivantes :
       - bouton-droit sur le package "training.mtalha.rest_api_spring_boot_k8s" et créez un sous-package "rest.controller"
       - bouton-droit sur le package "rest.controller" et créez la classe MyRestApi
       - Voici le code source de la classe [MyRestApi](https://github.com/Cloud-Elit/Docker_Kubernetes/blob/main/rest-api-spring-boot-k8s/src/main/java/training/mtalha/rest_api_spring_boot_k8s/rest/controller/MyRestApi.java)
       - Générez le package du projet : ```mvn clean package -DskipTests=true```
         <br/><b>Remarque : </b>Vérifiez que le package <b>rest-api-spring-boot-k8s-0.0.1-SNAPSHOT.jar</b> est bien généré dans le répertoire : <b>rest-api-spring-boot-k8s\target</b>. En cas de problème, vérifiez votre JDK (dans File/Project Structure...) et Maven utilisé (dans File/Settings).
         <br/><br/>Voici une capture d'écran du projet complet (y compris les fichiers des ateliers qui suivent) :<br/>
	 ![Capture](https://github.com/user-attachments/assets/86ca77de-f0ce-46bf-9d92-95423d5a96f2)    	

## Atelier 3. Créer le Dockerfile et générer l'Image Docker
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé Dockerfile
2. Complétez le Dockerfile avec les commandes suivantes :<br/>
```
FROM openjdk:17-alpine
COPY ./target/rest-api-spring-boot-k8s-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT exec java -jar app.jar --debug
```
3. Ouvrez une console PowerShell (ou l'invite de commande) lancez minikube avec la commande suivante : ```minikube start```
4. Vérifiez que le cluster K8s est bien démarré : ```minikube status```
<br/><b>Remarque : </b>Vous devriez obtenir le résultat suivant :<br/>
![Capture](https://github.com/user-attachments/assets/7944b417-8e1b-4166-8e71-6020771dbbf6)
5. Accédez au noeud du cluster K8s en ssh : ```minikube ssh```
<br/>Un terminal ssh de minikube est alors ouvert pour pouvoir exécuter des commandes linux
6. Vérifiez que Docker est bien installé : ```docker version```
7. Listez les images existantes : ```docker image ls```
8. Générez l'image grâce à l'utiliser <b><i>build</i></b> de Docker :
  - Psitionnez-vous dans votre répertoire de travail du projet Java : <br/>E.g. ```cd /c/Users/Mohamed/Downloads/rest-api-spring-boot-k8s```
  - Exécutez la commande build pour générer l'image Docker : <br/>E.g. ```docker build --file=Dockerfile --tag=rest-api-spring-boot-k8s:1.0.0 .```
  - Vérifiez l'image : ```docker image ls```
  - Vous devriez obtenir un résultat comme celui-là :<br/>
![Capture](https://github.com/user-attachments/assets/a2d58574-61d5-479c-b3cc-6a6cd5be25f3)
  - Quittez le terminal ssh : ```exit```
		
## Atelier 4. Créer les objets K8s (Deployment et Service) nécessaires à la création et l'exposition du pod
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé deployment-basic.yaml
2. Complétez le deployment-basic.yaml comme suit :<br/>
```
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
          image: docker.io/library/rest-api-spring-boot-k8s:1.0.0
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
```
<br/><b>Remarque : </b>Pour plus de détails sur l'écriture d'un Deployment, allez sur [Writing a Deployment Spec](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/#writing-a-deployment-spec)<br/>
3. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé service.yaml
4. Complétez le service.yaml comme suit :<br/>
```
apiVersion: v1
kind: Service
metadata:
  name: rest-api-spring-boot-k8s-service
spec:
  selector:
    app: rest-api-spring-boot-k8s
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
  type: NodePort
```

## Atelier 5. Déployer et lancer l'API REST sur minikube
1. Sur la console PowerShell (ou l'invite de commande) positionnez-vous dans votre répertoire de travail du projet Java : <br\>E.g. ```cd C:\Users\Mohamed\Downloads\rest-api-spring-boot-k8s```
2. Créez le Deployment grâce à la commane <i>apply</i> : ```minikube kubectl -- apply -f deployment-basic.yaml```<br/><b>Remarque : </b>Un message confirmant la création du déploiment doit être affiché : "deployment.apps/rest-api-spring-boot-k8s created"
3. Vérifiez que le déployment est bien créé :```minikube kubectl -- get deployments```<br/>Cette commande doit vous lister des informations sur le Deployment comme suit :<br/>
![Capture1](https://github.com/user-attachments/assets/4ae6cad2-dea5-4013-adc8-a995094dc77a)
4. Vérifiez qu'un pod a bien été lancé et qu'il est en status RUNNING : ```minikube kubectl -- get pods```<br/>Cette commande doit vous lister au moins un pod (si replicas = 1) :<br/>
![Capture2](https://github.com/user-attachments/assets/fb14fb68-c068-423e-a20a-25abb2c7b09b)
5. Affichez les informations du pod : E.g. ```minikube kubectl -- describe pod rest-api-spring-boot-k8s-7899bf44b6-tktn8```<br/>
![Capture3](https://github.com/user-attachments/assets/b6ed8e96-b7f0-4a73-a62b-5dd4a6c48e87)
<br/>Comme vous pouvez le remarquer, le pod est <b><u>contrôlé par un ReplicaSet</u></b>, que vous pouvez gérer (affichier, supprimer pour créer un autre, etc.) : ```minikube kubectl -- get replicasets```
6. Affichez les logs du pod : E.g. ```minikube kubectl -- logs rest-api-spring-boot-k8s-7899bf44b6-c6fj4```<br/>
Analysez les logs et vérifiez qu'il n'y a pas d'erreur dans l'application. En cas d'erreur, il faudra revoir le code source, regénérer le jar, re-build le Dockerfile pour avoir une nouvelle image, etc. 		
7. Exposez votre service via la commande suivante : ```minikube kubectl -- apply -f service.yaml```
<br/>Vous pouvez vérifier que le service est bien créé : ```minikube kubectl -- get services```
8. A ce stade, l'application est déployée, le service est exposé, vous pouvez récupérer l'URL du service grâce à la commande suivante : ```minikube service rest-api-spring-boot-k8s-service --url```
<br/>Cette commande vous renvoit l'URL du service :<br/>
![Capture6](https://github.com/user-attachments/assets/e85496d4-52ad-4e57-a987-2ff9fa35f242)
9. Dans un navigateur web, accédez à l'endpoint de votre API : E.g. ```http://192.168.59.100:31344/home/info```
![Capture](https://github.com/user-attachments/assets/62d6c5b8-8afb-4280-b01c-5c165baf0f76)

## Atelier 6. Créer, déployer et utiliser un ConfigMap
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé deployment-configmap.yaml
2. Complétez le deployment-configmap.yaml comme suit :<br/>
```
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
          image: docker.io/library/rest-api-spring-boot-k8s:1.0.0
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
          env:
            - name: DB_HOST
              valueFrom:
                configMapKeyRef:
                  name: rest-api-spring-boot-k8s-configmap
                  key: dbHost
            - name: DB_NAME
              valueFrom:
                configMapKeyRef:
                  name: rest-api-spring-boot-k8s-configmap
                  key: dbName
            - name: DB_PORT
              valueFrom:
                configMapKeyRef:
                  name: rest-api-spring-boot-k8s-configmap
                  key: dbPort
```
3. Appliquez le nouveau déploiement grâce à la commande <i>apply</i> : ```minikube kubectl -- apply -f deployment-configmap.yaml```
4. Vérifiez qu'un nouveau pod a bien créé et qu'il est en status RUNNING : ```minikube kubectl -- get pods```
5. N'ayant pas besoin de réexposer le service, dans un navigateur web, accédez à l'endpoint de votre API : E.g. ```http://192.168.59.100:31344/home/env```

## Atelier 7. Créer, déployer et utiliser un Secret en tant que variable d'environnement
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé deployment-secret-env.yaml
2. Complétez le deployment-secret-env.yaml comme suit :<br/>
```
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
          image: docker.io/library/rest-api-spring-boot-k8s:1.0.0
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
          env:
            - name: env.namespace
              value: default
            - name: DB_HOST
              valueFrom:
                configMapKeyRef:
                  name: rest-api-spring-boot-k8s-configmap
                  key: dbHost
            - name: DB_NAME
              valueFrom:
                configMapKeyRef:
                  name: rest-api-spring-boot-k8s-configmap
                  key: dbName
            - name: DB_PORT
              valueFrom:
                configMapKeyRef:
                  name: rest-api-spring-boot-k8s-configmap
                  key: dbPort
            - name: DB_USER
              valueFrom:
                secretKeyRef:
                  name: rest-api-spring-boot-k8s-secret
                  key: dbUserName
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: rest-api-spring-boot-k8s-secret
                  key: dbPassword
```
3. Appliquez le nouveau déploiement grâce à la commande <i>apply</i> : ```minikube kubectl -- apply -f deployment-secret-env.yaml```
4. Vérifiez qu'un nouveau pod a bien créé et qu'il est en status RUNNING : ```minikube kubectl -- get pods```
5. N'ayant pas besoin de réexposer le service, dans un navigateur web, accédez à l'endpoint de votre API : E.g. ```http://192.168.59.100:31344/home/env```

## Atelier 8. Créer, déployer et utiliser un Secret en tant que fichier monté dans un volume
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé deployment-secret-file.yaml
2. Complétez le deployment-secret-file.yaml comme suit :<br/>
```
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
          image: docker.io/library/rest-api-spring-boot-k8s:1.0.0
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
          env:
            - name: env.namespace
              value: default
            - name: DB_HOST
              valueFrom:
                configMapKeyRef:
                  name: rest-api-spring-boot-k8s-configmap
                  key: dbHost
            - name: DB_NAME
              valueFrom:
                configMapKeyRef:
                  name: rest-api-spring-boot-k8s-configmap
                  key: dbName
            - name: DB_PORT
              valueFrom:
                configMapKeyRef:
                  name: rest-api-spring-boot-k8s-configmap
                  key: dbPort
            - name: DB_USER
              valueFrom:
                secretKeyRef:
                  name: rest-api-spring-boot-k8s-secret
                  key: dbUserName
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: rest-api-spring-boot-k8s-secret
                  key: dbPassword
          volumeMounts:
            - name: application-secrets
              mountPath: "/etc/secret" #accessible via minikube kubectl -- exec -it rest-api-spring-boot-k8s-6ccc44b4c4-rfz9b -- ls /etc/secret
              readOnly: true
      volumes:
        - name: application-secrets
          secret:
            secretName: rest-api-spring-boot-k8s-secret
```
3. Appliquez le nouveau déploiement grâce à la commande <i>apply</i> : ```minikube kubectl -- apply -f deployment-secret-file.yaml```
4. Vérifiez qu'un nouveau pod a bien créé et qu'il est en status RUNNING : ```minikube kubectl -- get pods```
5. Vérifiez qu'un volume a bien été monté dans le répertoire <b>/etc/secret</b> du pod : ```minikube kubectl -- exec -it rest-api-spring-boot-k8s-6ccc44b4c4-rfz9b -- ls /etc/secret```
   <br/>Cette commande vous permet de vous connecter sur le pod en mode interactif et exécuter la commande ls. Voici le résultat de la commande :<br/>
6. Ouvrez le ficher dbPassword pour afficher le mot de passe : ```minikube kubectl -- exec -it rest-api-spring-boot-k8s-6ccc44b4c4-rfz9b -- cat /etc/secret/dbPassword```
   <br/>Voici le résultat attendu de la commande :<br/>
