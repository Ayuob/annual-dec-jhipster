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

describe('AnnualDeclaration e2e test', () => {
  const annualDeclarationPageUrl = '/annual-declaration';
  const annualDeclarationPageUrlPattern = new RegExp('/annual-declaration(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const annualDeclarationSample = { submissionDate: '2023-08-23', status: 'REJECTED' };

  let annualDeclaration;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/annual-declarations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/annual-declarations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/annual-declarations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (annualDeclaration) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/annual-declarations/${annualDeclaration.id}`,
      }).then(() => {
        annualDeclaration = undefined;
      });
    }
  });

  it('AnnualDeclarations menu should load AnnualDeclarations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('annual-declaration');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AnnualDeclaration').should('exist');
    cy.url().should('match', annualDeclarationPageUrlPattern);
  });

  describe('AnnualDeclaration page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(annualDeclarationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AnnualDeclaration page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/annual-declaration/new$'));
        cy.getEntityCreateUpdateHeading('AnnualDeclaration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', annualDeclarationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/annual-declarations',
          body: annualDeclarationSample,
        }).then(({ body }) => {
          annualDeclaration = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/annual-declarations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/annual-declarations?page=0&size=20>; rel="last",<http://localhost/api/annual-declarations?page=0&size=20>; rel="first"',
              },
              body: [annualDeclaration],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(annualDeclarationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AnnualDeclaration page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('annualDeclaration');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', annualDeclarationPageUrlPattern);
      });

      it('edit button click should load edit AnnualDeclaration page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AnnualDeclaration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', annualDeclarationPageUrlPattern);
      });

      it('edit button click should load edit AnnualDeclaration page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AnnualDeclaration');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', annualDeclarationPageUrlPattern);
      });

      it('last delete button click should delete instance of AnnualDeclaration', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('annualDeclaration').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', annualDeclarationPageUrlPattern);

        annualDeclaration = undefined;
      });
    });
  });

  describe('new AnnualDeclaration page', () => {
    beforeEach(() => {
      cy.visit(`${annualDeclarationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AnnualDeclaration');
    });

    it('should create an instance of AnnualDeclaration', () => {
      cy.get(`[data-cy="submissionDate"]`).type('2023-08-22').blur().should('have.value', '2023-08-22');

      cy.get(`[data-cy="status"]`).select('REJECTED');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        annualDeclaration = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', annualDeclarationPageUrlPattern);
    });
  });
});
