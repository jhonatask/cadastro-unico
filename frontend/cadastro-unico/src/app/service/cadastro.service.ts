import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private apiUrl = 'http://localhost/8080/'; 

  constructor(private http: HttpClient) {}

  cadastrarCliente(cliente: any) {
    return this.http.post(`${this.apiUrl}clientes`, cliente);
  }
}
