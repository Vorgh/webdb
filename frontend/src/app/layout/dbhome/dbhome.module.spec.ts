import { DBHomeModule } from './dbhome.module';

describe('DBHomeModule', () => {
  let homeModule: DBHomeModule;

  beforeEach(() => {
    homeModule = new DBHomeModule();
  });

  it('should create an instance', () => {
    expect(homeModule).toBeTruthy();
  });
});
