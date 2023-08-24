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

describe('Beneficiary e2e test', () => {
  const beneficiaryPageUrl = '/beneficiary';
  const beneficiaryPageUrlPattern = new RegExp('/beneficiary(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const beneficiarySample = { entitlementType: 'PENSION' };

  let beneficiary;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/beneficiaries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/beneficiaries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/beneficiaries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (beneficiary) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/beneficiaries/${beneficiary.id}`,
      }).then(() => {
        beneficiary = undefined;
      });
    }
  });

  it('Beneficiaries menu should load Beneficiaries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('beneficiary');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Beneficiary').should('exist');
    cy.url().should('match', beneficiaryPageUrlPattern);
  });

  describe('Beneficiary page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(beneficiaryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Beneficiary page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/beneficiary/new$'));
        cy.getEntityCreateUpdateHeading('Beneficiary');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beneficiaryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/beneficiaries',
          body: beneficiarySample,
        }).then(({ body }) => {
          beneficiary = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/beneficiaries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/beneficiaries?page=0&size=20>; rel="last",<http://localhost/api/beneficiaries?page=0&size=20>; rel="first"',
              },
              body: [beneficiary],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(beneficiaryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Beneficiary page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('beneficiary');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beneficiaryPageUrlPattern);
      });

      it('edit button click should load edit Beneficiary page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Beneficiary');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beneficiaryPageUrlPattern);
      });

      it('edit button click should load edit Beneficiary page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Beneficiary');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beneficiaryPageUrlPattern);
      });

      it('last delete button click should delete instance of Beneficiary', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('beneficiary').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', beneficiaryPageUrlPattern);

        beneficiary = undefined;
      });
    });
  });

  describe('new Beneficiary page', () => {
    beforeEach(() => {
      cy.visit(`${beneficiaryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Beneficiary');
    });

    it('should create an instance of Beneficiary', () => {
      cy.get(`[data-cy="entitlementType"]`).select('OTHER');

      cy.get(`[data-cy="entitlementDetails"]`).type('dynamic USB Maine').should('have.value', 'dynamic USB Maine');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        beneficiary = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', beneficiaryPageUrlPattern);
    });
  });
});
