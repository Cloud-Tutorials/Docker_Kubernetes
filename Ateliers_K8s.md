# Ateliers de prise en main de Kubernetes
#### Environnement de travail :
	- Windows 8.1
	- JDK 17.0.12	
	- Maven 3.6.3
	- VirtualBox 6.1
	- IntelliJ IDEA 2021.3.3 (Community Edition)

 #### Key Words :
	- Container, Docker, Kubernetes, minikube, kubectl
	- API REST, Sping Boot
	- Helm, Vault	
 
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
	 ![image](https://github.com/user-attachments/assets/b2bf26c0-0add-44ce-8612-e5ba2bca6807)



## Atelier 3. Créer le Dockerfile et générer l'Image Docker
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé Dockerfile (il n'ya pas besoin de préciser une extension du fichier)
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
8. Générez l'image grâce à l'utilitaire <b><i>build</i></b> de Docker :
  - Psitionnez-vous dans votre répertoire de travail du projet Java : <br/>E.g. ```cd /c/Users/Mohamed/Downloads/K8S_REST_API/rest-api-spring-boot-k8s```
  - Exécutez la commande build pour générer l'image Docker : <br/>E.g. ```docker build --file=Dockerfile --tag=rest-api-spring-boot-k8s:1.0.0 .```
  - Vérifiez l'image : ```docker image ls```
  - Vous devriez obtenir un résultat comme celui-là :<br/>
![Capture](https://github.com/user-attachments/assets/a2d58574-61d5-479c-b3cc-6a6cd5be25f3)
  - Quittez le terminal ssh : ```exit```
		
## Atelier 4. Créer les objets K8s (Deployment et Service) nécessaires à la création et l'exposition du pod
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé deployment-basic.yaml
2. Complétez le Manifest deployment-basic.yaml comme suit :<br/>
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
<b>Remarque : </b>Pour plus de détails sur l'écriture d'un Deployment, allez sur [Writing a Deployment Spec](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/#writing-a-deployment-spec)<br/>
3. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé service.yaml<br/>
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
1. Sur la console PowerShell (ou l'invite de commande) positionnez-vous dans votre répertoire de travail du projet Java :<br/>E.g. ```cd C:/Users/Mohamed/Downloads/K8S_REST_API/rest-api-spring-boot-k8s```
3. Créez le Deployment grâce à la commane <i>apply</i> : ```minikube kubectl -- apply -f deployment-basic.yaml```<br/><b>Remarque : </b>Un message confirmant la création du déploiment doit être affiché : "deployment.apps/rest-api-spring-boot-k8s created"
4. Vérifiez que le déployment est bien créé :```minikube kubectl -- get deployments```<br/>Cette commande doit vous lister des informations sur le Deployment comme suit :<br/>
![Capture1](https://github.com/user-attachments/assets/4ae6cad2-dea5-4013-adc8-a995094dc77a)
5. Vérifiez qu'un pod a bien été lancé et qu'il est en status RUNNING : ```minikube kubectl -- get pods```<br/>Cette commande doit vous lister au moins un pod (si replicas = 1) :<br/>
![Capture2](https://github.com/user-attachments/assets/fb14fb68-c068-423e-a20a-25abb2c7b09b)
6. Affichez les informations du pod : E.g. ```minikube kubectl -- describe pod rest-api-spring-boot-k8s-7899bf44b6-tktn8```<br/>
![Capture3](https://github.com/user-attachments/assets/b6ed8e96-b7f0-4a73-a62b-5dd4a6c48e87)
<br/>Comme vous pouvez le remarquer, le pod est <b><u>contrôlé par un ReplicaSet</u></b>, que vous pouvez gérer (affichier, supprimer pour créer un autre, etc.) : ```minikube kubectl -- get replicasets```
7. Affichez les logs du pod : E.g. ```minikube kubectl -- logs rest-api-spring-boot-k8s-7899bf44b6-c6fj4```<br/>
Analysez les logs et vérifiez qu'il n'y a pas d'erreur dans l'application. En cas d'erreur, il faudra revoir le code source, regénérer le jar, re-build le Dockerfile pour avoir une nouvelle image, etc. 		
8. Exposez votre service via la commande suivante : ```minikube kubectl -- apply -f service.yaml```
<br/>Vous pouvez vérifier que le service est bien créé : ```minikube kubectl -- get services```
9. A ce stade, l'application est déployée, le service est exposé, vous pouvez récupérer l'URL du service grâce à la commande suivante : ```minikube service rest-api-spring-boot-k8s-service --url```
<br/>Cette commande vous renvoit l'URL du service :<br/>
![Capture6](https://github.com/user-attachments/assets/e85496d4-52ad-4e57-a987-2ff9fa35f242)
10. Dans un navigateur web, accédez à l'endpoint de votre API : E.g. ```http://192.168.59.100:31344/home/info```
![Capture](https://github.com/user-attachments/assets/04097a7e-bf2c-4b2d-be74-4f7d05f80e21)

## Atelier 6. Créer, déployer et utiliser un ConfigMap
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé configmap.yaml
2. Complétez le configmap.yaml comme suit :
```
apiVersion: v1
kind: ConfigMap
metadata:
  name: rest-api-spring-boot-k8s-configmap
data:
  dbHost: "db.training.mtalha.fr"
  dbName: "mydb"
  dbPort: "12345"
```
3. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé deployment-configmap.yaml
4. Complétez le Manifest deployment-configmap.yaml comme suit :<br/>
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
5. Créez le ConfigMap grâce à la commande <i>apply</i> : ```minikube kubectl -- apply -f configmap.yaml```
6. Mettez-à-jour le déploiement grâce à la commande <i>apply</i> : ```minikube kubectl -- apply -f deployment-configmap.yaml```
7. Vérifiez qu'un nouveau pod a bien créé et qu'il est en status RUNNING : ```minikube kubectl -- get pods```
8. Affichez les détails du pod et vérifiez ses variables d'environnement : E.g. ```minikube kubectl -- describe pod rest-api-spring-boot-k8s-6b8779c8cd-twhx8```
   <br/>![Capture](https://github.com/user-attachments/assets/87eecb54-1bb8-446c-9c3a-0ec500fd0032)
9. N'ayant pas besoin de réexposer le service, dans un navigateur web, accédez à l'endpoint de votre API pour afficher les variables d'environnement : E.g. ```http://192.168.59.100:31344/home/env```<br/>
![Capture](https://github.com/user-attachments/assets/90b8c353-c0f3-4716-9346-c85fbf88e78a)

## Atelier 7. Créer, déployer et utiliser un Secret en tant que variable d'environnement
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé secret.yaml
2. Complétez le secret.yaml comme suit :
```
apiVersion: v1
kind: Secret
metadata:
  name: rest-api-spring-boot-k8s-secret
data:
  dbUserName: "bXRhbGhh" # "mtalha" encodé en base64
  dbPassword: "VEFMSEFfUGFzc3cwcmQ=" # "TALHA_Passw0rd" encodé en base64
```
3. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé deployment-secrets-env.yaml
4. Complétez le Manifest deployment-secrets-env.yaml comme suit :<br/>
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
5. Créez le Secret grâce à la commande apply : ```minikube kubectl -- apply -f secret.yaml```
6. Mettez-à-jour le déploiement grâce à la commande <i>apply</i> : ```minikube kubectl -- apply -f deployment-secrets-env.yaml```
7. Vérifiez qu'un nouveau pod a bien créé et qu'il est en status RUNNING : ```minikube kubectl -- get pods```
8. Affichez les détails du pod et vérifiez de nouveau les variables d'environnement : E.g. ```minikube kubectl -- describe pod rest-api-spring-boot-k8s-6cdcf644c7-6nmn8```
![Capture](https://github.com/user-attachments/assets/b360db81-90a8-403c-b594-45345292365d)
9. N'ayant pas besoin de réexposer le service, dans un navigateur web, accédez à l'endpoint de votre API : E.g. ```http://192.168.59.100:31344/home/secrets```<br/>
![Capture](https://github.com/user-attachments/assets/f81a4556-0047-464f-9161-e530a821e457)

## Atelier 8. Créer, déployer et utiliser un Secret en tant que fichier monté dans un volume
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé deployment-secrets-file.yaml
2. Complétez le Manifest deployment-secrets-file.yaml comme suit :<br/>
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
3. Le Secret existe déjà, il suffit de mettre-à-jour le déploiement grâce à la commande <i>apply</i>:<br/> ```minikube kubectl -- apply -f deployment-secrets-file.yaml```
4. Vérifiez qu'un nouveau pod a bien créé et qu'il est en status RUNNING : ```minikube kubectl -- get pods```
5. Affichez les détails du pod et vérifiez qu'un nouveau volumen a été monté sur /etc/secret : E.g. ```minikube kubectl -- describe pod rest-api-spring-boot-k8s-6ccc44b4c4-rfz9b```
   ![Capture](https://github.com/user-attachments/assets/cac5c9dc-4740-46bd-b62e-5655148e17fd)
7. Vérifiez qu'un volume a bien été monté dans le répertoire <b>/etc/secret</b> du pod : ```minikube kubectl -- exec -it rest-api-spring-boot-k8s-6ccc44b4c4-rfz9b -- ls /etc/secret```
   <br/>Cette commande vous permet de vous connecter sur le pod en mode interactif et exécuter la commande ls.
8. Ouvrez le ficher dbPassword pour afficher le mot de passe : ```minikube kubectl -- exec -it rest-api-spring-boot-k8s-6ccc44b4c4-rfz9b -- cat /etc/secret/dbPassword```
   <br/>Voici le résultat attendu des deux dernières commandes :
![Capture](https://github.com/user-attachments/assets/6fde91ef-1595-484c-80c0-8cbeba811acd)

## Atelier 9. Installer Helm et HashiCorp Vault sur le Cluster Kubernetes
### Installation d'Helm
<b>Helm</b> est un 'gestionnaire de paquets' pour Kubernetes permettant de définir, d’installer et de mettre à jour des applications Kubernetes grâce à ses chartes. Une <b>Chart Helm</b> est un paquet contentant un ensemble 'cohérent' et 'reproductible' d’applications et de leurs dépendances.
<br/>Sur une machine Windows, Helm peut être installé en téléchargeant l’exécutable depuis le site officiel d’[Helm](https://helm.sh/docs/intro/install/) et en l’ajoutant à la variable d’environnement PATH. <br/>L’installation d’Helm peut être vérifiée en affichant la version installée : ```helm version```
### Installation de HashiCorp Vault
HashiCorp Vault peut être installé depuis le dépôt Helm de HashiCorp. Pour cela, il faut :
1. ajouter le référentiel HashiCorp Helm à votre configuration Helm grâce à la commande :<br/> ```helm repo add hashicorp https://helm.releases.hashicorp.com```
2. et ensuite installer Vault à l'aide d'une Chart Helm du référentiel HashiCorp avec la commande suivante :<br/>
```
helm install vault hashicorp/vault --set='server.dev.enabled=true' --set='ui.enabled=true' --set='ui.serviceType=LoadBalancer'
```
<br/>L’installation de Vault peut être vérifiée en affichant ses pods : ```minikube kubectl -- get all```
<br/>Remarquons que nous disposons de deux pods :
 - un pod <b>vault-0</b> qui gère les Secrets
 - un pod <b>vault-agent-injector</b> qui se charge de récupérer les Secrets et de les injecter dans les pods applicatifs 'autorisés'
 
## Atelier 10. Gérer les Secrets K8s avec HashiCorp Vault
Cette étape consiste à configurer les politiques de sécurité et les méthodes d'authentification de Vault pour gérer les accès aux Secrets. Cela permet de garantir que seuls les pods autorisés peuvent récupérer les données sensibles de Vault.
<br/>Pour effectuer la configuration initiale, il faut se connecter au pod <b>vault-0</b> avec la commande suivante :<br/>```minikube kubectl -- exec -it vault-0 -- /bin/sh```
### Créer et Appliquer une politique de sécurité
Une fois connecté au pod <b>vault-0</b>, il faut y créer une politique autorisant la lecture des secrets. Cette politique sera associée à un rôle permettant d'accorder l'accès à des comptes de service Kubernetes spécifiques. Les politiques sont écrites en HCL (Hashicorp Configuration Language) ou JSON et décrivent les chemins dans Vault auxquels une machine ou un utilisateur est autorisé à accéder. Voici un exempe de création d'une politique :<br/>
```
cat <<EOF > /home/vault/read-policy.hcl
path "secret*" {
  capabilities = ["read"]
}
EOF
```
<br/>Cette politique peut-être appliquée comme suit : ```vault policy write read-policy /home/vault/read-policy.hcl```
<br/>D'une manière générale, voici la syntaxe permettant d'appliquer une politique : ```vault policy write <policy-name> /path/to/policy.hcl```
### Activer et Configurer l'authentification Kubernetes
Il ne faut pas oublier d'activer la méthode d’authentification Kubernetes dans Vault. Voici la commande : ```vault auth enable kubernetes```
<br/>Pour communiquer avec le serveur API Kubernetes, il faut également appliquer la configuration Vault comme suit :
```
vault write auth/kubernetes/config \
   token_reviewer_jwt="$(cat /var/run/secrets/kubernetes.io/serviceaccount/token)" \
   kubernetes_host=https://${KUBERNETES_PORT_443_TCP_ADDR}:443 \
   kubernetes_ca_cert=@/var/run/secrets/kubernetes.io/serviceaccount/ca.crt
```
### Créer un rôle
Enfin, il faut créer un rôle (ex. <b>vault-role</b>) qui lie la politique ci-dessus (ex. <b>read-policy</b>) à un compte de service Kubernetes (ex. <b>rest-api-spring-boot-k8s-service-account</b>) dans un espace de noms spécifique (dans ce tutotiel, en se contente de l'espace de nom par défaut, i.e. <b>default</b>). Cela permet au compte de service d'accéder aux secrets stockés dans Vault pendant une durée spécifiée via le paramètre TTL (Time-To-Live). Voici la commande :<br/>
```
vault write auth/kubernetes/role/vault-role \
   bound_service_account_names=rest-api-spring-boot-k8s-service-account \
   bound_service_account_namespaces=default \
   policies=read-policy \
   ttl=1h
```
<br/>Notez qu'il est possible de spécifier plusieurs comptes de services et plusieurs espaces de noms; Voici la syntaxe : 
```
vault write auth/kubernetes/role/<my-role> \
   bound_service_account_names=sa1, sa2 \
   bound_service_account_namespaces=namespace1, namespace2 \
   policies=<policy-name> \
   ttl=<duration>
```
### Créer des Secrets dans Vault
Les secrets peuvent être créés via la CLI Vault, en se connectant au pod <b>vault-0</b>, ou plus simplement via l’<b>UI Vault</b>. L'URL de l'UI Vault est celle du service <b>vault-ui</b> qui peut être obtenue grâce à la commande : ```minikube service vault-ui --url```
Exemple :
![Capture](https://github.com/user-attachments/assets/dc8c594d-c031-4f68-be11-fbf611c34147)
<br/>Vault UI est donc accessible via l’adresse : http://192.168.59.105:31897
![image](https://github.com/user-attachments/assets/a0642442-ec47-4a4d-b5bd-5c5d953dda49)
<br/>Pour se connecter, choisissez la méthode <b>Token</b> et saisissez la valeur <b>root</b> comme valeur du token.
</br>Accédez à l'onglet <b>Secrets Engines > secret</b> et cliquez ensuite sur <b>Create secret</b> en haut à droite.
![image](https://github.com/user-attachments/assets/09554367-9c3c-4f26-ab1b-d94e51120225)
<br/>Renseignez ensuite les champs requis pour créer un secret :
![image](https://github.com/user-attachments/assets/14079110-2fe6-46b5-8438-265c7a98d90d)
<br/>Cliquez enfin sur "Save" pour créer le secret. Et voilà, vous avez un premier Secret appelé "my-vault-secret" stocké dans Vault et contenant deux entrées "username" et "password".
### Accéder à un Secret Vault depuis un pod applicatif
A ce stade, nous avons :
- installé Vault et configuré un rôle Vault (ex. <b>vault-role</b>) pour permettre au compte de service (ex. <b>rest-api-spring-boot-k8s-service-account</b>) d’accéder aux secrets stockés dans Vault.
- créé un Secret <b>my-vault-secret</b> avec deux paires clé-valeur.

Nous allons maintenant créer un compte de service et l'associer à un POD pour accéder à ce Secret.
#### Créer un compte de service (Service Account)
Ce compte de service dispose d’autorisations pour le rôle Vault tel que défini dans l’étape « Créer un rôle » ci-dessus.
Comme nous l'avons défini à l'étape "Créer un rôle", nous avons besoin d'un Service Account appelé <b>rest-api-spring-boot-k8s-service-account</b> lié au rôle <b>vault-role</b>. Ce Service Account peut être créé comme suit :
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé service-account.yaml
2. Complétez le service-account.yaml comme suit :<br/>
```
apiVersion: v1
kind: ServiceAccount
metadata:
  name: rest-api-spring-boot-k8s-service-account
  labels:
    app: rest-api-spring-boot-k8s
```
3. Créez le Service Account grâce à la commande apply : ```minikube kubectl -- apply -f service-account.yaml```
#### Créer un pod applicatif ayant accès au Secret dans Vault
1. Sur IntelliJ, à la racine de votre projet (au même niveau que le pom.xml), créez un fichier vide appelé deployment-secrets-vault.yaml
2. Complétez le Manifest deployment-secrets-vault.yaml comme suit :<br/>
```
#how to write a deployment spec : https://kubernetes.io/docs/concepts/workloads/controllers/deployment/#writing-a-deployment-spec
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
      annotations:
        vault.hashicorp.com/agent-inject: "true" #enable Vault Agent injection for this pod.
        vault.hashicorp.com/agent-inject-status: "update" #ensure the status of secret injection is updated.
        vault.hashicorp.com/agent-inject-secret-my-vault-secret: "secret/my-vault-secret" #the secret stored at secret/my-vault-secret in Vault should be injected
        vault.hashicorp.com/agent-inject-template-my-vault-secret: | #the template for the injected my-vault-secret, specifying the format in which the secret will be written.
          {{- with secret "secret/my-vault-secret" -}}
          username={{ .Data.data.username }}
          password={{ .Data.data.password }}
          {{- end }}
        vault.hashicorp.com/role: "vault-role" #the Vault role to be used for authentication
    spec:
      serviceAccountName: rest-api-spring-boot-k8s-service-account #the service account that has permissions to access Vault.
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
<b>Remarque : </b>Ce manifeste de déploiement crée une réplique (replicas: 1) d'un pod rest-api-spring-boot-k8s configuré pour récupérer en toute sécurité les secrets de Vault. L'agent Vault injecte le secret my-vault-secret dans le pod, conformément aux règles de sécurité spécifiées. Les secrets sont stockés dans le système de fichiers du pod et sont accessibles à l'application exécutée dans le conteneur. Le compte de service rest-api-spring-boot-k8s-service-account, doté des autorisations nécessaires, est utilisé pour l'authentification auprès de Vault.<br/>
3. Mettez-à-jour le déploiement grâce à la commande <i>apply</i> : ```minikube kubectl -- apply -f deployment-secrets-vault.yaml```<br/>
4. Vérifiez qu'un nouveau pod a bien créé et qu'il est en status RUNNING : ```minikube kubectl -- get pods```<br/>
5. Affichez les détails du pod : E.g. ```minikube kubectl -- describe pod rest-api-spring-boot-k8s-67c89c675c-qb8tf```<br/>
et vérifiez l'existence de :
<br/>
6. Connectez-vous au pod applicatif :
  <br/>E.g.
  ```
  minikube kubectl -- exec -it rest-api-spring-boot-k8s-67c89c675c-qb8tf -- /bin/sh
  ```
7. Vous pouvez afficher le Secret <b>my-vault-secret</b> qui se trouve dans le voulme <b>/vault/secrets/</b>
  <br/>Voici un exemple :<br/>
 ![image](https://github.com/user-attachments/assets/a75aea84-f6ec-40cb-9073-7bb84312049b)







