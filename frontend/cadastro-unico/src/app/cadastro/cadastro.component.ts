import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, AbstractControl, ValidationErrors} from '@angular/forms';
import { ClienteService } from '../service/cadastro.service';

@Component({
  selector: 'app-cadastro',
  templateUrl: './cadastro.component.html',
  styleUrl: './cadastro.component.css'
})
export class CadastroComponent {
  clienteForm!: FormGroup;

  constructor(private fb: FormBuilder, private clienteService: ClienteService) {
    this.clienteForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(11)], this.nomeNaoRedundanteValidator.bind(this)],
      cpf: ['', [Validators.required, this.cpfValidator.bind(this)]],
      endereco: ['', Validators.required],
      bairro: ['', Validators.required],
      telefones: this.fb.array([
        this.criarTelefoneFormGroup()
      ])
    });
  }
  
  ngOnInit(): void {
  }

  get telefones(): FormArray {
    return this.clienteForm.get('telefones') as FormArray;
  }

  adicionarTelefone() {
    this.telefones.push(this.criarTelefoneFormGroup());
  }

  criarTelefoneFormGroup() {
    return this.fb.group({
      numero: ['', Validators.required]  
    });
  }

  removerTelefone(index: number) {
    this.telefones.removeAt(index);
  }

  nomeNaoRedundanteValidator(control: { value: any; }) {
    const nome = control.value;
   
    if (nome === 'NomeRedundante') {
      return { nomeRedundante: true };
    }
   
    return null;
  }
  
  cpfValidator(control: AbstractControl) {
    const cpf = control.value;
    if (cpf && cpf.length === 11) {
      if (!this.validarCPF(cpf)) {
        return { cpfInvalido: true };
      }
    }
    return null;
  }

  validarCPF(cpf: string): boolean {
    cpf = cpf.replace(/\D/g, '');

  if (cpf.length !== 11) {
    return false;
  }

  if (/^(\d)\1+$/.test(cpf)) {
    return false;
  }

  let soma = 0;
  for (let i = 0; i < 9; i++) {
    soma += parseInt(cpf.charAt(i), 10) * (10 - i);
  }
  let resto = 11 - (soma % 11);
  let digitoVerificador1 = resto === 10 || resto === 11 ? 0 : resto;

  soma = 0;
  for (let i = 0; i < 10; i++) {
    soma += parseInt(cpf.charAt(i), 10) * (11 - i);
  }
  resto = 11 - (soma % 11);
  let digitoVerificador2 = resto === 10 || resto === 11 ? 0 : resto;

  return digitoVerificador1 === parseInt(cpf.charAt(9), 10) && digitoVerificador2 === parseInt(cpf.charAt(10), 10);
  }

  salvarCliente() {
    const cliente = this.clienteForm.value;

    this.clienteService.cadastrarCliente(cliente).subscribe(
      (response) => {
        alert('Cliente cadastrado com sucesso!');
      },
      (error) => {
        alert('Erro ao cadastrar cliente:');
      }
    );
  }
  

}
