export class AuthValidator {
  static isLoginDataValid(loginData: any): boolean {
    if (!loginData) return false;

    const { username, password } = loginData;
    const validUsername = typeof username === 'string' && username.trim().length > 2;
    const validPassword = typeof password === 'string' && password.trim().length > 3;

    return validUsername && validPassword;
  }
}
