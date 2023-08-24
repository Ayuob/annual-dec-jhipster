import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('SocialSecurityPensioner e2e test', () => {
  const socialSecurityPensionerPageUrl = '/social-security-pensioner';
  const socialSecurityPensionerPageUrlPattern = new RegExp('/social-security-pensioner(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const socialSecurityPensionerSample = {
    nationalNumber: 'clicks-and-m',
    pensionNumber: 'Plastic XSS Handcrafted',
    dateOfBirth: '2023-08-23',
    address: 'Fish',
  };

  let socialSecurityPensioner;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/social-security-pensioners+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/social-security-pensioners').as('postEntityRequest');
    cy.intercept('DELETE', '/api/social-security-pensioners/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (socialSecurityPensioner) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/social-security-pensioners/${socialSecurityPensioner.id}`,
      }).then(() => {
        socialSecurityPensioner = undefined;
      });
    }
  });

  it('SocialSecurityPensioners menu should load SocialSecurityPensioners page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('social-security-pensioner');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SocialSecurityPensioner').should('exist');
    cy.url().should('match', socialSecurityPensionerPageUrlPattern);
  });

  describe('SocialSecurityPensioner page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(socialSecurityPensionerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SocialSecurityPensioner page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/social-security-pensioner/new$'));
        cy.getEntityCreateUpdateHeading('SocialSecurityPensioner');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialSecurityPensionerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/social-security-pensioners',
          body: socialSecurityPensionerSample,
        }).then(({ body }) => {
          socialSecurityPensioner = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/social-security-pensioners+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/social-security-pensioners?page=0&size=20>; rel="last",<http://localhost/api/social-security-pensioners?page=0&size=20>; rel="first"',
              },
              body: [socialSecurityPensioner],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(socialSecurityPensionerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SocialSecurityPensioner page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('socialSecurityPensioner');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialSecurityPensionerPageUrlPattern);
      });

      it('edit button click should load edit SocialSecurityPensioner page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialSecurityPensioner');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialSecurityPensionerPageUrlPattern);
      });

      it('edit button click should load edit SocialSecurityPensioner page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialSecurityPensioner');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialSecurityPensionerPageUrlPattern);
      });

      it('last delete button click should delete instance of SocialSecurityPensioner', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('socialSecurityPensioner').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', socialSecurityPensionerPageUrlPattern);

        socialSecurityPensioner = undefined;
      });
    });
  });

  describe('new SocialSecurityPensioner page', () => {
    beforeEach(() => {
      cy.visit(`${socialSecurityPensionerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SocialSecurityPensioner');
    });

    it('should create an instance of SocialSecurityPensioner', () => {
      cy.get(`[data-cy="nationalNumber"]`).type('HandmadeXXXX').should('have.value', 'HandmadeXXXX');

      cy.get(`[data-cy="pensionNumber"]`).type('Keyboard Branding hack').should('have.value', 'Keyboard Branding hack');

      cy.get(`[data-cy="dateOfBirth"]`).type('2023-08-23').blur().should('have.value', '2023-08-23');

      cy.get(`[data-cy="address"]`).type('Forks driver Virginia').should('have.value', 'Forks driver Virginia');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        socialSecurityPensioner = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', socialSecurityPensionerPageUrlPattern);
    });
  });
});
