import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private apiUrl = 'http://localhost:8080/'; 
  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Headers':
        'Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
      'Content-Type': 'application/json; charset=UTF-8',
    }),
  };
  constructor(private http: HttpClient) {}

  cadastrarCliente(cliente: any) {
    return this.http.post(`${this.apiUrl}clientes`, cliente, this.httpOptions);
  }
}
