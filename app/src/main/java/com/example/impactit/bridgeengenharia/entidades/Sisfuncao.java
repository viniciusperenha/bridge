/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 *
 * @author munif
 */

public class Sisfuncao implements Serializable {

    private Long id;
    private String nome;


    public Sisfuncao() {
    }

    public Sisfuncao(Long id) {
        this.id = id;
    }

    public Sisfuncao(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sisfuncao)) {
            return false;
        }
        Sisfuncao other = (Sisfuncao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + " " + nome;
    }
    
}
