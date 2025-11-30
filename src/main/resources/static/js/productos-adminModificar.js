// JS extracted from productos-adminModificar.html
// Handles client-side validation and filling fields when selecting a product

// Validación de descripción: solo letras (incluye acentos y Ñ/ñ) y espacios
function validateDescripcion(el) {
    var regex = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]*$/;
    var clientError = document.getElementById('descripcionClientError');
    if (!regex.test(el.value)) {
        el.setCustomValidity('La descripción solo puede contener letras y espacios.');
        if (clientError) { clientError.style.display = 'inline'; }
    } else {
        el.setCustomValidity('');
        if (clientError) { clientError.style.display = 'none'; }
    }
}

// Rellenar campos al seleccionar un producto
document.addEventListener('DOMContentLoaded', function() {
    var select = document.getElementById('selectAnadirEjemplar');
    function fillFieldsFromOption(opt) {
        if (!opt) return;
        document.getElementById('id').value = opt.getAttribute('data-id') || '';
        document.getElementById('codigo').value = opt.getAttribute('data-codigo') || '';
        document.getElementById('nombre').value = opt.getAttribute('data-nombre') || '';
        document.getElementById('descripcion').value = opt.getAttribute('data-descripcion') || '';
        document.getElementById('categoria').value = opt.getAttribute('data-categoria') || '';
        document.getElementById('marca').value = opt.getAttribute('data-marca') || '';
        document.getElementById('precio').value = opt.getAttribute('data-precio') || '';
    }
    if (select) {
        // If a product is already selected on server-side, fill fields
        var initial = select.querySelector('option[selected]');
        if (initial) { fillFieldsFromOption(initial); }
        select.addEventListener('change', function() {
            var opt = select.options[select.selectedIndex];
            fillFieldsFromOption(opt);
        });
    }

    var descripcion = document.getElementById('descripcion');
    if (descripcion) {
        descripcion.addEventListener('input', function(){ validateDescripcion(this); });
    }

    var codigo = document.getElementById('codigo');
    if (codigo) { codigo.addEventListener('input', function(){ this.setCustomValidity(''); }); }

    // --- NEW: submit formulario por AJAX para quedarse en la misma página ---
    var form = document.querySelector('form.formulario');
    if (form) {
        // create container for ajax messages (place it after the form to avoid layout issues)
        var ajaxContainer = document.getElementById('mensajeAjax');
        if (!ajaxContainer) {
            ajaxContainer = document.createElement('div');
            ajaxContainer.id = 'mensajeAjax';
            // insert after the form
            if (form.parentNode) {
                if (form.nextSibling) form.parentNode.insertBefore(ajaxContainer, form.nextSibling);
                else form.parentNode.appendChild(ajaxContainer);
            }
        }

        form.addEventListener('submit', function(e){
            // If user holds Ctrl/Shift (force normal submit) allow it
            if (e.ctrlKey || e.shiftKey) return;
            e.preventDefault();
            ajaxContainer.innerHTML = '';

            var submitBtn = form.querySelector('button[type=submit]');
            if (submitBtn) { submitBtn.disabled = true; submitBtn.dataset.origText = submitBtn.innerHTML; submitBtn.innerHTML = 'Procesando...'; }

            var formData = new FormData(form);

            // Attempt to find CSRF token input in the form and set a header if found
            var csrfInput = form.querySelector('input[type="hidden"][name*="csrf"]');
            var headers = {
                'X-Requested-With': 'XMLHttpRequest'
            };
            if (csrfInput && csrfInput.value) {
                headers['X-CSRF-TOKEN'] = csrfInput.value;
            }

            // fetch the action (same as the form), include X-Requested-With to hint server
            fetch(form.action, {
                method: 'POST',
                body: formData,
                headers: headers,
                credentials: 'same-origin'
            }).then(function(response){
                return response.text();
            }).then(function(text){
                // parse returned HTML and extract any success/error messages
                var parser = new DOMParser();
                var doc = parser.parseFromString(text, 'text/html');

                // Prefer server-side rendered .alert elements
                var successEl = doc.querySelector('.alert.alert-success');
                var errorEl = doc.querySelector('.alert.alert-danger');

                if (successEl) {
                    ajaxContainer.innerHTML = '';
                    var clone = successEl.cloneNode(true);
                    clone.style.marginBottom = '10px';
                    ajaxContainer.appendChild(clone);

                    // update the currently selected option dataset and text/value from the form values
                    var selectedOpt = select ? select.options[select.selectedIndex] : null;
                    if (selectedOpt) {
                        // Update attributes with current form values (normalize to what server likely stored)
                        var newCodigo = (document.getElementById('codigo') && document.getElementById('codigo').value) || selectedOpt.getAttribute('data-codigo');
                        var newNombre = (document.getElementById('nombre') && document.getElementById('nombre').value) || selectedOpt.getAttribute('data-nombre');
                        var newDescripcion = (document.getElementById('descripcion') && document.getElementById('descripcion').value) || selectedOpt.getAttribute('data-descripcion');
                        var newCategoria = (document.getElementById('categoria') && document.getElementById('categoria').value) || selectedOpt.getAttribute('data-categoria');
                        var newMarca = (document.getElementById('marca') && document.getElementById('marca').value) || selectedOpt.getAttribute('data-marca');
                        var newPrecio = (document.getElementById('precio') && document.getElementById('precio').value) || selectedOpt.getAttribute('data-precio');

                        selectedOpt.setAttribute('data-codigo', newCodigo);
                        selectedOpt.setAttribute('data-nombre', newNombre);
                        selectedOpt.setAttribute('data-descripcion', newDescripcion);
                        selectedOpt.setAttribute('data-categoria', newCategoria);
                        selectedOpt.setAttribute('data-marca', newMarca);
                        selectedOpt.setAttribute('data-precio', newPrecio);

                        // update option value and visible text
                        selectedOpt.value = newCodigo;
                        selectedOpt.text = newCodigo + ' - ' + newNombre;

                        // set the form fields to server-normalized values if available in doc
                        var returnedProduct = doc.querySelector('input[name="id"][value]');
                        // try to read updated producto fields from returned HTML (if controller added them)
                        // fallback uses current form values already set
                    }

                } else if (errorEl) {
                    ajaxContainer.innerHTML = '';
                    var cloneErr = errorEl.cloneNode(true);
                    cloneErr.style.marginBottom = '10px';
                    ajaxContainer.appendChild(cloneErr);
                } else {
                    // If no explicit alerts found, try to find mensaje/error model fragments
                    var altSuccess = doc.getElementById('mensaje');
                    var altError = doc.getElementById('error');
                    if (altSuccess && altSuccess.textContent.trim()) {
                        ajaxContainer.innerHTML = '<div class="alert alert-success" style="margin-bottom:10px;">' + altSuccess.textContent + '</div>';
                    } else if (altError && altError.textContent.trim()) {
                        ajaxContainer.innerHTML = '<div class="alert alert-danger" style="margin-bottom:10px;">' + altError.textContent + '</div>';
                    } else {
                        // Generic fallback
                        ajaxContainer.innerHTML = '<div class="alert alert-info" style="margin-bottom:10px;">La petición se procesó. Comprueba si los datos se han actualizado.</div>';
                    }
                }
            }).catch(function(err){
                ajaxContainer.innerHTML = '<div class="alert alert-danger" style="margin-bottom:10px;">Error al procesar la petición: ' + (err.message || err) + '</div>';
            }).finally(function(){
                if (submitBtn) { submitBtn.disabled = false; if (submitBtn.dataset.origText) submitBtn.innerHTML = submitBtn.dataset.origText; }
            });
        });

        // adicional: el botón no submit ejecutará el mismo comportamiento
        var btnAjax = document.getElementById('btnModificarAjax');
        if (btnAjax) {
            btnAjax.addEventListener('click', function(){
                // trigger submit programmatically
                var ev = new Event('submit', {bubbles: true, cancelable: true});
                form.dispatchEvent(ev);
            });
        }
    }
});
