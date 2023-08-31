document.addEventListener('DOMContentLoaded', function () {
    const button = document.getElementById('mybutton');

    button.addEventListener('click', async function (event) {
        event.preventDefault();

        const nombre = document.getElementById('nombre').value;
        const apellido = document.getElementById('apellido').value;
        const ci = document.getElementById('ci').value;
        const edad = document.getElementById('edad').value;
        const imagenInput = document.getElementById('imagen');
        const imagenFile = imagenInput.files[0];

        const reader = new FileReader();

        reader.onload = async function () {
            const imagenBase64 = reader.result.split(',')[1];

            // Construye un objeto con los datos del formulario
            const formData = {
                nombre: nombre,
                apellido: apellido,
                ci: ci,
                edad: edad,
                imagen: imagenBase64
            };

            try {
                // Convierte el objeto a una cadena JSON
                const jsonData = JSON.stringify(formData);

                const response = await fetch('http://localhost:8080/persona', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: jsonData
                });

                if (response.ok) {
                    console.log('Solicitud POST enviada exitosamente');
                } else {
                    console.error('Error al enviar la solicitud POST');
                }
            } catch (error) {
                console.error('Error en la solicitud:', error);
            }
        };

        reader.readAsDataURL(imagenFile);
    });
});
