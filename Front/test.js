const { Builder, By, until } = require('selenium-webdriver');
require('chromedriver');

(async function createEventTest() {
let driver = new Builder().forBrowser('chrome').usingServer('http://localhost:4444/wd/hub').build();

  try {
    // Étape 1: Ouvrir la page principale (localhost:4200)
    await driver.get('http://localhost:4200');
    console.log("Page d'accueil ouverte...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 2: Attendre que le lien Login soit visible et cliquer dessus
    let loginLink = await driver.wait(until.elementLocated(By.css('a[routerLink="/login"]')), 10000);
    console.log("Lien 'Login' trouvé...");
    await loginLink.click();
    console.log("Clique sur le lien Login...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 3: Attendre que la page de login soit complètement chargée
    await driver.wait(until.elementLocated(By.css('.auth-form')), 10000);
    console.log("Page de login chargée...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 4: Remplir les champs de login
    let usernameField = await driver.findElement(By.id('username'));
    await usernameField.sendKeys('testuser@example.com'); // Saisir le nom d'utilisateur
    console.log("Nom d'utilisateur saisi...");
    await driver.sleep(1000); // Attendre 1 seconde avant de passer à l'étape suivante

    let passwordField = await driver.findElement(By.id('password'));
    await passwordField.sendKeys('newpassword'); // Saisir le mot de passe
    console.log("Mot de passe saisi...");
    await driver.sleep(1000); // Attendre 1 seconde avant de passer à l'étape suivante

    // Étape 5: Soumettre le formulaire
    let submitButton = await driver.findElement(By.css('button[type="submit"]'));
    await submitButton.click();
    console.log("Formulaire soumis...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 6: Attendre que l'alerte apparaisse et cliquer sur "OK"
    let alert = await driver.wait(until.alertIsPresent(), 10000); // Attendre que l'alerte soit présente
    console.log("Alerte détectée...");
    await alert.accept(); // Cliquer sur "OK"
    console.log("Alerte fermée avec OK...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 7: Après la connexion, l'utilisateur est redirigé vers le dashboard
    // Nous n'avons pas besoin d'attendre, car la redirection se fait automatiquement

    // Étape 8: Cliquer sur "Create Event"
    let createEventLink = await driver.wait(until.elementLocated(By.css('a[routerLink="/create-event"]')), 10000);
    console.log("Lien 'Create Event' trouvé...");
    await createEventLink.click();
    console.log("Clique sur le lien 'Create Event'...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 9: Attendre que le formulaire "Create Event" soit visible
    await driver.wait(until.elementLocated(By.css('.event-form')), 10000);
    console.log("Formulaire 'Create Event' chargé...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 10: Remplir le formulaire pour créer un événement
    let titleField = await driver.findElement(By.id('title'));
    await titleField.sendKeys('Mon événement test'); // Saisir le titre de l'événement
    console.log("Titre de l'événement saisi...");

    let descriptionField = await driver.findElement(By.id('description'));
    await descriptionField.sendKeys('Ceci est un test pour créer un événement.'); // Saisir la description
    console.log("Description de l'événement saisie...");

    let dateField = await driver.findElement(By.id('date'));
    await dateField.sendKeys('21/03/2026'); // Saisir la date de l'événement
    console.log("Date de l'événement saisie...");

    let locationField = await driver.findElement(By.id('location'));
    await locationField.sendKeys('Paris, France'); // Saisir la localisation
    console.log("Emplacement de l'événement saisi...");
    await driver.executeScript("window.scrollTo(0, document.body.scrollHeight)"); // Faire défiler la page vers le bas

    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 11: Soumettre le formulaire pour créer l'événement
    let createEventButton = await driver.findElement(By.css('button[type="submit"]'));
    await createEventButton.click();
    console.log("Événement créé...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 12: Attendre que l'alerte apparaisse et cliquer sur "OK"
    let alert1 = await driver.wait(until.alertIsPresent(), 10000); // Attendre que l'alerte soit présente
    console.log("Alerte détectée...");
    await alert1.accept(); // Cliquer sur "OK"
    console.log("Alerte fermée avec OK...");
    await driver.sleep(2000); // Attendre 2 secondes avant de passer à l'étape suivante

    // Étape 13: Assurer un défilement fluide
    await driver.executeScript("document.body.style.scrollBehavior = 'smooth';");
    await driver.executeScript("window.scrollTo(0, document.body.scrollHeight)"); 
    await driver.sleep(2000); // Attendre 2 secondes avant de passer à l'étape suivante

    // Étape 14: Cliquer sur "My Event" pour accéder à la liste des événements
    let myEventLink = await driver.wait(until.elementLocated(By.css('a[routerLink="/my-event"]')), 10000);
    console.log("Lien 'My Event' trouvé...");
    await myEventLink.click();
    console.log("Clique sur le lien 'My Event'...");
    await driver.sleep(2000); // Attendre 2 secondes pour vérifier la redirection

    // Vérifier si la page "My Event" est chargée
    console.log("Redirection réussie vers la page 'My Event' !");

    // Étape 15: Cliquer sur le bouton "Edit" du premier événement
    let firstEditButton = await driver.wait(until.elementLocated(By.css('table tbody tr:first-child td button')), 10000);  // Sélecteur pour le 1er bouton "Edit"
    console.log("Bouton 'Edit' du premier élément trouvé...");
    await firstEditButton.click();
    console.log("Clique sur le bouton 'Edit' du premier événement...");

    await driver.sleep(2000);  // Attendre 2 secondes pour voir l'effet du clic

    // Étape 16: Attendre que le formulaire "Edit Event" soit visible
    await driver.wait(until.elementLocated(By.css('.edit-event')), 10000);
    console.log("Formulaire 'Edit Event' chargé...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 17: Remplir le formulaire pour modifier l'événement
    let titleField1 = await driver.findElement(By.id('title'));
    await titleField1.sendKeys('Update'); // Saisir le titre de l'événement
    console.log("Titre de l'événement modifié...");

    await driver.executeScript("window.scrollTo(0, document.body.scrollHeight)"); // Faire défiler la page vers le bas

    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 18: Soumettre le formulaire pour mettre à jour l'événement
    let updateEventButton = await driver.findElement(By.css('button[type="submit"]'));
    await updateEventButton.click();
    console.log("Événement modifié...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

    // Étape 19: Attendre que l'alerte apparaisse et cliquer sur "OK"
    console.log("Alerte détectée...");
    await alert1.accept(); // Cliquer sur "OK"
    console.log("Alerte fermée avec OK...");
    await driver.sleep(2000); // Attendre 2 secondes avant de passer à l'étape suivante

    // Étape 20: Cliquer sur le bouton "Delete" du premier événement
    let firstDeleteButton = await driver.wait(until.elementLocated(By.css('table tbody tr:first-child td button:nth-child(2)')), 10000);  // Sélecteur pour le 1er bouton "Delete"
    console.log("Bouton 'Delete' du premier élément trouvé...");
    await firstDeleteButton.click();
    console.log("Clique sur le bouton 'Delete' du premier événement...");

    await driver.sleep(2000);  // Attendre 2 secondes pour voir l'effet du clic

    // Étape 21: Attendre que l'alerte de confirmation apparaisse et cliquer sur "OK"
    console.log("Alerte de confirmation détectée...");
    await alert1.accept(); // Cliquer sur "OK"
    console.log("Alerte fermée avec OK...");
    await driver.sleep(2000); // Attendre 2 secondes avant de passer à l'étape suivante

    // Étape 22: Retourner à la page Dashboard
    let dashEventLink = await driver.wait(until.elementLocated(By.css('a[routerLink="/dashboard"]')), 10000);
    console.log("Lien 'Dash Event' trouvé...");
    await dashEventLink.click();
    console.log("Clique sur le lien 'Dash Event'...");
    await driver.sleep(1500); // Attendre 1.5 secondes avant de passer à l'étape suivante

  } catch (error) {
    console.log('Erreur pendant l\'exécution du test :', error);
  } finally {
    // Fermer le navigateur
    await driver.quit();
  }
})();
